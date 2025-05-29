package com.example.demo.exception.userException;

public class UserNotFoundException extends UserException{
	public UserNotFoundException(String message) {
		super(message);
	}
	public UserNotFoundException() {
		super("帳號資料錯誤");
	}
}
