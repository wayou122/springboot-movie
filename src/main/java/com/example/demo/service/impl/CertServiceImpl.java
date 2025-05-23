package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.userException.PasswordWrongException;
import com.example.demo.exception.userException.UserException;
import com.example.demo.exception.userException.UserNotFoundException;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.dto.UserLoginDto;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CertService;
import com.example.demo.util.Hash;

@Service
public class CertServiceImpl implements CertService {
	@Autowired
	UserRepository userRepository;

	@Override
	public UserCert getCert(String email, String password) {
		if(email == null || password == null) {
			throw new UserException("請填寫必要欄位");
		}
		User user = userRepository.findByEmail(email)
				.orElseThrow(()->new UserNotFoundException("信箱尚未註冊"));
		if (!user.getPasswordHash().equals(Hash.getHash(password,user.getSalt()))) {
			throw new PasswordWrongException("密碼錯誤");
		}
		UserCert userCert = new UserCert(user.getUserId(), user.getUsername(), user.getEmailVerf(), user.getRole());
		return userCert;
	}
	
	@Override
	public UserCert getCert(UserLoginDto userLoginDto) {
		String email = userLoginDto.getEmail();
		String password = userLoginDto.getPassword();
		return getCert(email, password);
	}
}
