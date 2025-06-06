package com.example.demo.exception;

public class ParamsInvalidException extends RuntimeException{
	public ParamsInvalidException(String message) {
		super(message);
	}
	public ParamsInvalidException() {
		super("參數錯誤");
	}
}
