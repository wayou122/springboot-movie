package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.dto.UserRegisterDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tiann/register")
public class RegisterController {

	@Autowired
	UserService userService;
	
	@Autowired
	EmailService emailService;

	// 註冊
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> register(
			@ModelAttribute @Valid UserRegisterDto userRegisterDto,
			HttpSession httpSession) {
		//確認驗證碼
		String authcode = (String) httpSession.getAttribute("authcode");
		if(!authcode.equals(userRegisterDto.getAuthcode())) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"驗證碼錯誤"));
		}
		//寄送驗證信
		String token = userService.addUser(userRegisterDto);
		String email = userRegisterDto.getEmail();
		String confirmLink = "http://localhost:5173/email-confirm?email="+email+"&token="+token;
		//emailService.sendEmail(email, confirmLink);
		System.out.println(confirmLink);
		return ResponseEntity.ok(ApiResponse.success("註冊成功，請至信箱驗證"));
	}
	
	// 確認姓名可用
	@GetMapping("/check-username")
	public ResponseEntity<ApiResponse<Boolean>> checkUsername(
			@RequestParam String newUsername, @RequestParam String oldUsername) {
		Boolean isExisted = userService.existsByUsername(newUsername);
		if (newUsername.equals(oldUsername) || isExisted == false) {
			return ResponseEntity.ok(ApiResponse.success("名稱可用",true));			
		}else {
			return ResponseEntity.ok(ApiResponse.success("名稱重複",false));
		}
	}

	// 確認email可用
	@GetMapping("/check-email")
	public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email){
		Boolean isExisted = userService.existsByEmail(email);
		return ResponseEntity.ok(ApiResponse.success(!isExisted));
	}

	// 驗證email
	@GetMapping("/email-confirm")
	public ResponseEntity<ApiResponse<String>> emailConfirm(
			@RequestParam String email, @RequestParam String token) {
		userService.emailConfirm(email,token);
		return ResponseEntity.ok(ApiResponse.success("信箱驗證成功"));
	}
	
}
