package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.UserCert;
import com.example.demo.model.dto.UserLoginDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.AuthcodeService;
import com.example.demo.service.CertService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tiann")
public class LoginController {

	@Autowired
	private CertService certService;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Void>> postLogin(
			@ModelAttribute @Valid UserLoginDto userLoginDto,
			HttpSession httpSession){
		//確認登入狀態
		if (httpSession.getAttribute("userCert")!=null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"已經登入"));
		}
		//確認驗證碼
		String authcode = (String) httpSession.getAttribute("authcode");
		if(!authcode.equals(userLoginDto.getAuthcode())) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"驗證碼錯誤"));
		}
		//設定登入憑證
		UserCert userCert = certService.getCert(userLoginDto);
		httpSession.setAttribute("userCert",userCert);
		return ResponseEntity.ok(ApiResponse.success("登入成功"));
	}
	
	@GetMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(
			HttpSession httpSession){
		//確認登出狀態
		if (httpSession.getAttribute("userCert")==null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"已經登出"));
		}
		//清除登入憑證
		httpSession.invalidate();
		return ResponseEntity.ok(ApiResponse.success("登出成功"));
	}
	
	@GetMapping("/check-login")
	public ResponseEntity<ApiResponse<Boolean>> checkLogin(HttpSession httpSession){
		boolean loggedin = httpSession.getAttribute("userCert") != null;
		return ResponseEntity.ok(ApiResponse.success("檢查登入",loggedin));
	}
	
}
