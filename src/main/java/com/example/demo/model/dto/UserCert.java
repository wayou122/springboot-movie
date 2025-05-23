package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class UserCert {
	private Integer userId;
	private String username;
	private Boolean emailVerf;
	private String role;
}