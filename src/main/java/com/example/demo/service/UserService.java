package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import com.example.demo.model.dto.MovieCardDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.dto.UserLoginDto;
import com.example.demo.model.dto.UserRegisterDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
	UserDto getUserById(Integer userId);
	String addUser(UserRegisterDto userRegisterDto);
	String addUser(String username, String email, String password);
	void emailConfirm(String email, String token);
	Boolean checkEmailConfirm(String email);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	String setPasswordToken(String email);
	void resetPassword(String email, String token, String newPassword);
	void updateUsername(Integer userId, String username);
	void updateImage(Integer userId, MultipartFile image) throws IOException;
	void updateUserPassword(Integer userId, String password);
	void deleteUserById(Integer userId);

	Boolean toggleMovieForWatchlist(Integer userId, Integer movieId);
	List<MovieCardDto> getWatchlist(Integer userId);
}
