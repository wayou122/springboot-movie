package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
		String username = userRegisterDto.getUsername();
		String confirmLink = "http://localhost:8085/tiann/email/"+username+"/confirm?token="+token;
		
		//emailService.sendEmail(email, confirmLink);
		System.out.println(confirmLink);
		return ResponseEntity.ok(ApiResponse.success("註冊成功，請至信箱驗證"));
	}
	
	
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
	
}
