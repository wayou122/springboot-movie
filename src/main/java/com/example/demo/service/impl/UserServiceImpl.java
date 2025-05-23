package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.userException.UserExistedException;
import com.example.demo.exception.userException.UserNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.Hash;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	UserMapper userMapper;

	@Override
	public UserDto getUserById(Integer userId) {
		Optional<User> optUser = userRepository.findById(userId);
		if(optUser.isEmpty()) {
			throw new UserNotFoundException("使用者不存在");
		}
		return userMapper.toDto(optUser.get());
	}

	@Override
	public void updateUsername(Integer userId, String username) {
		if (existsByUsername(username)) {
			throw new UserExistedException("使用者名稱已被使用");
		}
		User user = userRepository.findById(userId).get();
		user.setUsername(username);		
		userRepository.save(user);
	}
	
	@Override
	public void updateUserPassword(Integer userId, String password) {
		User user = userRepository.findById(userId).get();
		String salt = Hash.getSalt();
		String passwordHash = Hash.getHash(password, salt);
		user.setPasswordHash(passwordHash);
		user.setSalt(salt);
		userRepository.save(user);
	}
	
	@Override
	public void updateUserNameAndPassword(Integer userId, String username, String password) {
		updateUsername(userId, username);
		updateUsername(userId, password);
	}

	@Override
	public void deleteUserById(Integer userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public Boolean existsByUsername(String username){
		return userRepository.existsByUsername(username);
	}


}
