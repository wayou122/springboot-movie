package com.example.demo.model.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

	@Email(message = "信箱格式不正確")
	private String email;
	private String password;
	private String authcode;
}
