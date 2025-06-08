package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.dto.UserCert;
import com.example.demo.model.dto.UserLoginDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.CertService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/tiann")
public class LoginController {

	@Autowired
	private CertService certService;
	@Autowired
	private UserService userService;

	// 登入
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Void>> postLogin(
			@ModelAttribute @Valid UserLoginDto userLoginDto,	HttpSession httpSession){
		//確認登入狀態
		if (httpSession.getAttribute("userCert")!=null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"已經登入"));
		}
		//確認驗證碼
		String authcode = (String) httpSession.getAttribute("authcode");
		if(!authcode.equals(userLoginDto.getAuthcode())) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"驗證碼錯誤"));
		}
		//確認信箱驗證
		if(!userService.checkEmailConfirm(userLoginDto.getEmail())){
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"信箱尚未驗證"));
		}
		//設定登入憑證
		UserCert userCert = certService.getCert(userLoginDto);
		httpSession.setAttribute("userCert",userCert);
		return ResponseEntity.ok(ApiResponse.success("登入成功"));
	}

	// 登出
	@GetMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(HttpSession httpSession){
		//確認登出狀態
		if (httpSession.getAttribute("userCert")==null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"已經登出"));
		}
		//清除登入憑證
		httpSession.invalidate();
		return ResponseEntity.ok(ApiResponse.success("登出成功"));
	}

	// 確認登入狀態
	@GetMapping("/check-login")
	public ResponseEntity<ApiResponse<Boolean>> checkLogin(HttpSession httpSession){
		boolean loggedin = httpSession.getAttribute("userCert") != null;
		return ResponseEntity.ok(ApiResponse.success("檢查登入",loggedin));
	}

	@PostMapping("/forget-password")
	public ResponseEntity<ApiResponse<Void>> forgetPassword(
			HttpSession httpSession, @RequestBody Map<String, String> request){
		String sessionAuthcode = (String) httpSession.getAttribute("authcode");
		String authcode = request.get("authcode");
		String email = request.get("email");
		//確認驗證碼
		if(!sessionAuthcode.equals(authcode)) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"驗證碼錯誤"));
		}
		//確認信箱狀態
		if(!userService.existsByEmail(email)){
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"此信箱尚未註冊"));
		}
		if(!userService.checkEmailConfirm(email)){
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"此信箱尚未驗證"));
		}
		//寄送重設連結
		String token = userService.setPasswordToken(email);
		String confirmLink = "http://localhost:5173/reset-password?email="+email+"&token="+token;
		//emailService.sendEmail(email, confirmLink);
		System.out.println(confirmLink);
		return ResponseEntity.ok(ApiResponse.success("已寄送重設密碼連結，請盡快至信箱點擊連結重設密碼"));
	}

	// 重設密碼
	@PostMapping("reset-password")
	public ResponseEntity<ApiResponse<Void>> resetPassword(
			@RequestBody Map<String, String> request){
		String email = request.get("email");
		String token = request.get("token");
		String newPassword = request.get("password");
		userService.resetPassword(email,token,newPassword);
		return ResponseEntity.ok(ApiResponse.success("密碼已重新設定，請重新登入"));
	}
}
