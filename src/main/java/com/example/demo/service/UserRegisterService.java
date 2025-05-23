package com.example.demo.service;

import com.example.demo.model.dto.UserRegisterDto;

public interface UserRegisterService {
	String addUser(UserRegisterDto userRegisterDto);
	String addUser(String username, String email, String password);
	Boolean emailConfirm(String username, String token);
}
