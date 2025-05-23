package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.response.ApiResponse;
import com.example.demo.service.UserRegisterService;

@Controller
@RequestMapping("/tiann/email")
public class EmailConfirmController {

	@Autowired
	UserRegisterService userRegisterService;

	@GetMapping("/{username}/confirm")
	public ResponseEntity<ApiResponse<String>> emailConfirm(
			@PathVariable String username, @RequestParam String token) {
		if(userRegisterService.emailConfirm(username,token)) {
			return ResponseEntity.ok(ApiResponse.success("信箱驗證成功"));
		}
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"信箱驗證錯誤"));
	}

}
