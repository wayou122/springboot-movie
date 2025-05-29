package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.ReviewDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/tiann/user/review")
public class ReviewController {
	
	@Autowired
	ReviewService reviewService;
	
	@PostMapping("/add/{movieId}")
	public ResponseEntity<ApiResponse<Void>> addReview(HttpSession httpSession,
			@ModelAttribute ReviewDto reviewDto, @PathVariable Integer movieId){
		UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
		Integer userId = userCert.getUserId();
		reviewService.addReview(reviewDto, userId, movieId);
		return ResponseEntity.ok(ApiResponse.success("新增評論成功"));
	}
	
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<ApiResponse<Void>> deleteReview(
			@PathVariable Integer reviewId, HttpSession httpSession){
		UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
		Integer userId = userCert.getUserId();
		reviewService.deleteReview(reviewId, userId);
		return ResponseEntity.ok(ApiResponse.success("刪除評論成功"));
	}
	
	@PatchMapping
	public ResponseEntity<ApiResponse<Void>> updateReview(
			@ModelAttribute ReviewDto reviewDto, HttpSession httpSession){
		UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
		Integer userId = userCert.getUserId();
		reviewService.updateReview(reviewDto, userId);
		return ResponseEntity.ok(ApiResponse.success("修改評論成功"));
	}
	
}
