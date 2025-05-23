package com.example.demo.service;

import java.io.IOException;

public interface AuthcodeService {
	String generateAuthcode();
	byte[] generateAuthcodeImg(String authcode) throws IOException;
}
