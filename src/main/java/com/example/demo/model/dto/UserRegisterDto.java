package com.example.demo.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegisterDto {
	
	@Pattern(regexp = "^[a-zA-Z0-9-_]{5,20}$", message = "帳號名稱格式不符")
	private String username;
	
	@Email(message = "信箱格式不正確")
	private String email;
	private String password;
	private String authcode;
	
}
