package com.example.demo.config;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.movieException.MovieException;
import com.example.demo.exception.reivewException.ReviewException;
import com.example.demo.exception.userException.UserException;
import com.example.demo.exception.userException.UserExistedException;
import com.example.demo.exception.userException.UserNotFoundException;
import com.example.demo.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException e){
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.NOT_FOUND,e.getMessage()));
	}
	
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ApiResponse<Void>> handleUserException(UserException e){
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
	}
	
	@ExceptionHandler(MovieException.class)
	public ResponseEntity<ApiResponse<Void>> handleMovieException(MovieException e){
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
	}
	
	@ExceptionHandler(ReviewException.class)
	public ResponseEntity<ApiResponse<Void>> handleReviewException(ReviewException e){
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
      String errorMessage = ex.getBindingResult().getFieldErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .findFirst()
              .orElse("格式驗證錯誤");
      return ResponseEntity.badRequest()
              .body(ApiResponse.error(HttpStatus.BAD_REQUEST,errorMessage));
  }
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleUserException(Exception e){
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
	}

}
