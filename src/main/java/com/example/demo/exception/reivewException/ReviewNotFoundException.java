package com.example.demo.exception.reivewException;

public class ReviewNotFoundException extends ReviewException {
	public ReviewNotFoundException() {
		super("評論不存在");
	}
	public ReviewNotFoundException(String message) {
		super(message);
	}

}
