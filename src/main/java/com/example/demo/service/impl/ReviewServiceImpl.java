package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AccessInvalidException;
import com.example.demo.exception.movieException.MovieNotFoundException;
import com.example.demo.exception.reivewException.ReviewExistedException;
import com.example.demo.exception.reivewException.ReviewNotFoundException;
import com.example.demo.exception.userException.UserNotFoundException;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.model.dto.ReviewDto;
import com.example.demo.model.entity.Movie;
import com.example.demo.model.entity.Review;
import com.example.demo.model.entity.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private ReviewMapper reviewMapper;
	
	@Override
	public void addReview(ReviewDto reviewDto, Integer userId, Integer movieId) {
		User user = userRepository.findById(userId)
				.orElseThrow(UserNotFoundException::new);
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(MovieNotFoundException::new);
		if (reviewRepository.existsByUserIdAndMovieId(userId, movieId)) {
			throw new ReviewExistedException();
		}
		Review review = reviewMapper.toEntity(reviewDto);
		review.setUser(user);
		review.setMovie(movie);
		reviewRepository.save(review);
	}

	@Override
	public void deleteReview(Integer reviewId, Integer userId) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(ReviewNotFoundException::new);
		if(review.getUser().getUserId() != userId) {
			throw new AccessInvalidException("沒有刪除權限");
		}
		reviewRepository.deleteById(reviewId);
	}

	@Override
	public void updateReview(ReviewDto reviewDto, Integer userId) {
		Review review = reviewRepository.findById(reviewDto.getReviewId())
				.orElseThrow(()-> new ReviewNotFoundException());
		if(review.getUser().getUserId() != userId) {
			throw new AccessInvalidException("沒有修改權限");
		}
		review.setScore(reviewDto.getScore());
		review.setContent(reviewDto.getContent());
		reviewRepository.save(review);
	}
}
