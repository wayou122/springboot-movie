package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.UserRegisterDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserRegisterService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tiann/register")
@CrossOrigin(origins= {"http://localhost:5173"}, allowCredentials = "true")
public class RegisterController {

	@Autowired
	UserRegisterService userRegisterService;
	
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
		String token = userRegisterService.addUser(userRegisterDto);
		String username = userRegisterDto.getUsername();
		String confirmLink = "http://localhost:8085/tiann/email/"+username+"/confirm?token="+token;
		
		//emailService.sendEmail(email, confirmLink);
		System.out.println(confirmLink);
		return ResponseEntity.ok(ApiResponse.success("註冊成功，請至信箱驗證"));
	}
	
}
