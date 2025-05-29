package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.MovieCardDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.dto.UserRegisterDto;

public interface UserService {
	UserDto getUserById(Integer userId);
	String addUser(UserRegisterDto userRegisterDto);
	String addUser(String username, String email, String password);
	Boolean emailConfirm(String username, String token);
	Boolean existsByUsername(String username);
	void updateUsername(Integer userId, String username);
	void updateUserPassword(Integer userId, String password);
	void updateUserNameAndPassword(Integer userId, String username, String password);
	void deleteUserById(Integer userId);

	Boolean toggleMovieForWatchlist(Integer userId, Integer movieId);
	List<MovieCardDto> getWatchlist(Integer userId);
}
