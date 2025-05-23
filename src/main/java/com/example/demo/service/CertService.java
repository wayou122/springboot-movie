package com.example.demo.service;

import com.example.demo.model.dto.UserCert;
import com.example.demo.model.dto.UserLoginDto;

public interface CertService {
	UserCert getCert(String email, String password);
	UserCert getCert(UserLoginDto userLoginDto);
}
