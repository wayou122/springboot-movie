package com.example.demo.service;

import com.example.demo.model.dto.UserDto;

public interface UserService {
	UserDto getUserById(Integer userId);
	void updateUsername(Integer userId, String username);
	void updateUserPassword(Integer userId, String password);
	void updateUserNameAndPassword(Integer userId, String username, String password);
	void deleteUserById(Integer userId);
	Boolean existsByUsername(String username);

}
