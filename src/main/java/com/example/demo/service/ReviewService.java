package com.example.demo.service;

import com.example.demo.model.dto.ReviewDto;

public interface ReviewService {
	void addReview(ReviewDto reviewDto, Integer userId, Integer movieId);
	void deleteReview(Integer reviewId, Integer userId);
	void updateReview(ReviewDto reviewDto, Integer userId);
}
