package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.ReviewDto;
import com.example.demo.model.dto.ReviewMovieCardDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.ReviewReaction;
import com.example.demo.repository.ReviewReactionRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/tiann")
public class ReviewController {
	
	@Autowired
	ReviewService reviewService;
	
	//查詢所有評論
	@GetMapping("/review")
	public ResponseEntity<ApiResponse<Page<ReviewMovieCardDto>>> getAllReviewMovieCard(
			HttpSession httpSession,
			@RequestParam(defaultValue = "1") String page,
			@RequestParam(defaultValue = "all") String score){
		Integer userId = null;
		if (httpSession.getAttribute("userCert") != null) {
			userId = ((UserCert) httpSession.getAttribute("userCert")).getUserId();
		}
		
		// 頁數
		Integer pageInteger = 1;
		try {
			pageInteger = Integer.parseInt(page);
		}catch(NumberFormatException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"參數錯誤"));
		}
		Pageable pageable = PageRequest.of(pageInteger - 1, 10);
		
		// 分數篩選
		Integer scoreInteger = null;
		if (!score.equalsIgnoreCase("all")) {
			try {
				scoreInteger = Integer.parseInt(score);
				if (scoreInteger <1 || scoreInteger >5) {
					return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"參數錯誤"));
				}
			} catch(NumberFormatException e) {
				return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"參數錯誤"));
			}
		}
		
		Page<ReviewMovieCardDto> dtos = reviewService.getReviewMovieCardDtosPage(userId, pageable, scoreInteger);
		return ResponseEntity.ok(ApiResponse.success("查詢成功",dtos));
	}
	
	//新增評論
	@PostMapping("/user/review/{movieId}")
	public ResponseEntity<ApiResponse<Void>> addReview(HttpSession httpSession,
			@PathVariable Integer movieId, @ModelAttribute ReviewDto reviewDto){
		UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
		Integer userId = userCert.getUserId();
		reviewService.addReview(reviewDto, userId, movieId);
		return ResponseEntity.ok(ApiResponse.success("新增評論成功"));
	}
	
	//刪除評論
	@DeleteMapping("/user/review/{reviewId}")
	public ResponseEntity<ApiResponse<Void>> deleteReview(
			@PathVariable Integer reviewId, HttpSession httpSession){
		UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
		Integer userId = userCert.getUserId();
		reviewService.deleteReview(reviewId, userId);
		return ResponseEntity.ok(ApiResponse.success("刪除評論成功"));
	}
	
	//修改評論
	@PutMapping("/user/review/{reviewId}")
	public ResponseEntity<ApiResponse<Void>> updateReview(
			@ModelAttribute ReviewDto reviewDto,
			@PathVariable Integer reviewId, HttpSession httpSession){
		UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
		Integer userId = userCert.getUserId();
		reviewService.updateReview(reviewDto,reviewId, userId);
		return ResponseEntity.ok(ApiResponse.success("修改評論成功"));
	}
	
	//按讚評論
	@PutMapping("/user/review/reaction/{reviewId}/{reaction}")
	public ResponseEntity<ApiResponse<Void>> toggleReaction(HttpSession httpSession,
			@PathVariable Integer reviewId, @PathVariable Integer reaction){
		UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
		Integer userId = userCert.getUserId();
		reviewService.toggleReaction(userId, reviewId, reaction);
		return ResponseEntity.ok(ApiResponse.success("修改喜好成功"));
	}
	
}
