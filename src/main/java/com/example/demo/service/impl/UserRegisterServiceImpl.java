package com.example.demo.service.impl;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.userException.UserExistedException;
import com.example.demo.exception.userException.UserNotFoundException;
import com.example.demo.model.dto.UserRegisterDto;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserRegisterService;
import com.example.demo.util.Hash;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {
	@Autowired
	UserRepository userRepository;
	
	@Override
	public String addUser(UserRegisterDto userRegisterDto) {
		String username = userRegisterDto.getUsername();
		String email = userRegisterDto.getEmail();
		String password = userRegisterDto.getPassword();
		return addUser(username, email, password);
	}
	
	@Override
	public String addUser(String username, String email, String password) {
		if(userRepository.findByUsername(username).isPresent()) {
			throw new UserExistedException("使用者名稱已使用");
		}
		if(userRepository.findByEmail(email).isPresent()) {
			throw new UserExistedException("此信箱已註冊");
		}
		String salt = Hash.getSalt();
		String passwordHash = Hash.getHash(password, salt);
		
		SecureRandom random = new SecureRandom();
		String token = new BigInteger(130, random).toString(32);
		
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPasswordHash(passwordHash);
		user.setSalt(salt);
		user.setEmailVerfToken(token);
		userRepository.save(user);
		return token;
	}

	@Override
	public Boolean emailConfirm(String username, String token) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(()-> new UserNotFoundException("帳號尚未註冊"));
		if (user.getEmailVerfToken().equals(token)) {
			user.setEmailVerf(true);			
			userRepository.save(user);
			return true;
		}
		return false;
	}
}
