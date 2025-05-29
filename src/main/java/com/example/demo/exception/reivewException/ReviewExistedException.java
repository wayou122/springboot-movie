package com.example.demo.exception.reivewException;

public class ReviewExistedException extends ReviewException{
	public ReviewExistedException(String message) {
		super(message);
	}
	public ReviewExistedException() {
		super("評論已存在，無法新增");
	}
}
