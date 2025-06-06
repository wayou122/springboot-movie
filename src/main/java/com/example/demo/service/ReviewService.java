package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.example.demo.model.dto.ReviewDto;
import com.example.demo.model.dto.ReviewMovieCardDto;
import com.example.demo.model.entity.Review;

public interface ReviewService {
	void addReview(ReviewDto reviewDto, Integer userId, Integer movieId);
	void deleteReview(Integer reviewId, Integer userId);
	void updateReview(ReviewDto reviewDto, Integer reviewId, Integer userId);
	void toggleReaction(Integer userId, Integer reviewId, Integer reaction);
	Page<ReviewMovieCardDto> getReviewMovieCardDtosPage(Integer userId, Pageable pageable, Integer scoreFilter);
	List<ReviewDto> toDtoList (List<Review> reviews, Integer userId);
	Integer calculateLikeCount(Review review);


}
