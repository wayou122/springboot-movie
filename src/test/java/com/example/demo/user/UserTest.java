package com.example.demo.user;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.service.UserService;
import com.example.demo.util.Hash;

@SpringBootTest
public class UserTest {

	@Test
	public void get() {
		for (int i=0; i<10;i++) {
			SecureRandom random = new SecureRandom();
			String token = new BigInteger(130, random).toString(32);
			System.out.println(token);
		}
	}
	

}
