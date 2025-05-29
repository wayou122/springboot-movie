package com.example.demo.exception.movieException;

public class MovieNotFoundException extends MovieException {
	public MovieNotFoundException(String message) {
		super(message);
	}
	public MovieNotFoundException() {
		super("電影資料錯誤");
	}
}
