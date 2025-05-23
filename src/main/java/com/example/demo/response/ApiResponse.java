package com.example.demo.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
	private String status;
	private int code;
	private String message;
	private T data;

	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<T>("success", 200, message, data);
	}
	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<T>("success", 200, "操作成功", data);
	}
	public static <T> ApiResponse<T> success(String message) {
		return new ApiResponse<T>("success", 200, message, null);
	}
	
	public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message, T data){
		return new ApiResponse<T>("error", httpStatus.value(), message, data);
	}
	public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message){
		return new ApiResponse<T>("error", httpStatus.value(), message, null);
	}
}
