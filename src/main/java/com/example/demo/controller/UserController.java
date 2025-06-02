package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.MovieCardDto;
import com.example.demo.model.dto.MoviesFilterDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tiann/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/account")
	public ResponseEntity<ApiResponse<UserDto>> info(HttpSession httpSession){
		if (httpSession.getAttribute("userCert") == null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"尚未登入"));
		}
		UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
		UserDto userDto = userService.getUserById(userCert.getUserId()); 
		return ResponseEntity.ok(ApiResponse.success("使用者資訊",userDto));
	}
	
	@PutMapping("/update-username")
	public ResponseEntity<ApiResponse<UserDto>> updateUsername(
			@ModelAttribute @Valid UserUpdateDto userUpdateDto,
			HttpSession httpSession,
			BindingResult bindingResult){
		if (httpSession.getAttribute("userCert") == null) {
			return ResponseEntity.badRequest()
					.body(ApiResponse.error(HttpStatus.BAD_REQUEST,"尚未登入"));
		}
		UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
		String username = userService.getUserById(userCert.getUserId()).getUsername();
		if (userUpdateDto.getUsername() != null) {
			String newUsername = userUpdateDto.getUsername();
			if (!userService.existsByUsername(newUsername)) {
				userService.updateUsername(userCert.getUserId(),newUsername);				
			}else if (newUsername.equals(username)) {
				
			}else if(userService.existsByUsername(userUpdateDto.getUsername())) {
				return ResponseEntity.badRequest()
						.body(ApiResponse.error(HttpStatus.BAD_REQUEST,"帳號名稱重複"));
			}
		}
		return ResponseEntity.ok(ApiResponse.success("修改成功",null));
	}
	
	@PostMapping("/watchlist")
	public ResponseEntity<ApiResponse<List<MovieCardDto>>> aaa(
			@ModelAttribute MoviesFilterDto moviesFilterDto, HttpSession httpSession){
		if (httpSession.getAttribute("userCert") == null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"尚未登入"));
		}
		UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
		Integer userId = userCert.getUserId();
		List<MovieCardDto> dtos = userService.getWatchlist(userId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功",dtos));
	}
	
	@PutMapping("/toggle-watchlist/movie/{movieId}")
	public ResponseEntity<ApiResponse<Boolean>> toggleMovieForWatchlist(
			 @PathVariable Integer movieId, HttpSession httpSession){
		if (httpSession.getAttribute("userCert") == null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"尚未登入"));
		}
		UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
		Integer userId = userCert.getUserId();
		Boolean added = userService.toggleMovieForWatchlist(userId, movieId);
		return ResponseEntity.ok(ApiResponse.success(added));
	}
	
}
