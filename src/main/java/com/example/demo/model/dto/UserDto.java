package com.example.demo.model.dto;

import lombok.Data;

@Data
public class UserDto {
	private Integer userId;
	private String username;
	private String email;
	private Boolean emailVerf;
	private String role;
}
