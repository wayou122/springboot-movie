package com.example.demo.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateDto {
	
	@Pattern(regexp = "^[a-zA-Z0-9-_]{5,20}$", message = "帳號名稱格式不符")
	private String username;
	
	private String password;
	
	@Email(message = "信箱格式不正確")
	private String email;
}
