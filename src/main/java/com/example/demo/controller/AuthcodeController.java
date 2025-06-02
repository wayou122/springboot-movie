package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.AuthcodeService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/tiann")
public class AuthcodeController {
	@Autowired
	AuthcodeService authcodeService;
	
	@GetMapping("/authcode")
	public byte[] getAuthcode(HttpSession httpSession) throws IOException {
		String authcode = authcodeService.generateAuthcode();
		byte[] img = authcodeService.generateAuthcodeImg(authcode);
		httpSession.setAttribute("authcode",authcode);
		return img;
	}
	
}
