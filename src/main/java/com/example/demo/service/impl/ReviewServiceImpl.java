package com.example.demo.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.exception.ParamsInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AccessInvalidException;
import com.example.demo.exception.movieException.MovieNotFoundException;
import com.example.demo.exception.reivewException.ReviewExistedException;
import com.example.demo.exception.reivewException.ReviewNotFoundException;
import com.example.demo.exception.userException.UserNotFoundException;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.model.dto.ReviewDto;
import com.example.demo.model.dto.ReviewMovieCardDto;
import com.example.demo.model.entity.Movie;
import com.example.demo.model.entity.Review;
import com.example.demo.model.entity.ReviewReaction;
import com.example.demo.model.entity.ReviewReactionId;
import com.example.demo.model.entity.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.ReviewReactionRepository;
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
	private ReviewReactionRepository reviewReactionRepository;
	@Autowired
	private ReviewMapper reviewMapper;

	//新增評論
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

	//刪除評論
	@Override
	public void deleteReview(Integer reviewId, Integer userId) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(ReviewNotFoundException::new);
		if(!Objects.equals(review.getUser().getUserId(), userId)) {
			throw new AccessInvalidException("沒有刪除權限");
		}
		reviewRepository.deleteById(reviewId);
	}

	//更新評論
	@Override
	public void updateReview(ReviewDto reviewDto,Integer reviewId, Integer userId) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(ReviewNotFoundException::new);
		if(!Objects.equals(review.getUser().getUserId(), userId)) {
			throw new AccessInvalidException("沒有修改權限");
		}
		review.setScore(reviewDto.getScore());
		review.setContent(reviewDto.getContent());
		
		reviewRepository.save(review);
	}

	//按讚評論
	@Override
	public void toggleReaction(Integer userId, Integer reviewId, Integer reaction) {
		if (!userRepository.existsById(userId)) {
			throw new UserNotFoundException();
		}
		if (!reviewRepository.existsById(reviewId)) {
			throw new ReviewNotFoundException();
		}
		ReviewReactionId rrid =  new ReviewReactionId(userId, reviewId);
		Optional<ReviewReaction> optReviewReaction = reviewReactionRepository.findById(rrid);
		//首次點擊直接新增
		if (optReviewReaction.isEmpty()) {
			User user = userRepository.findById(userId).get();
			Review review = reviewRepository.findById(reviewId).get();
			ReviewReaction newReviewReaction = new ReviewReaction(rrid, user, review, reaction);
			reviewReactionRepository.save(newReviewReaction);
		} else {
			ReviewReaction existingReviewReaction = optReviewReaction.get();
			//若與原本相同就刪除
			if (existingReviewReaction.getReaction().equals(reaction)) {
				reviewReactionRepository.delete(existingReviewReaction);
			}else {
				existingReviewReaction.setReaction(reaction);
				reviewReactionRepository.save(existingReviewReaction);
			}
		}
	}

	//取得所有電影評論
	@Override
	public Page<ReviewMovieCardDto> getReviewMovieCardPage(
			Integer userId, String page, String sort, String score) {
		if (userId != null && !userRepository.existsById(userId)) {
			throw new UserNotFoundException();
		}
		// 頁數
		int pageInteger = 1;
		try {
			pageInteger = Integer.parseInt(page);
		}catch(NumberFormatException e) {
			pageInteger = 1;
		}
		// 排序
		Sort sortOption = switch (sort){
			case "new" -> Sort.by(Sort.Direction.DESC, "createdDate");
			case "popular" -> Sort.by(Sort.Direction.DESC, "likeCount");
			default -> throw new ParamsInvalidException();
		};
		Pageable pageable = PageRequest.of(pageInteger - 1, 10, sortOption);
		// 分數篩選
		Integer scoreInteger = null; // null表示所有分數
		if (!score.equalsIgnoreCase("all")) {
			try {
				scoreInteger = Integer.parseInt(score);
				if (scoreInteger <1 || scoreInteger >5) {
					throw new ParamsInvalidException("分數應在1~5之間");
				}
			} catch(NumberFormatException e) {
				throw new ParamsInvalidException();
			}
		}
		return reviewRepository.findAllReviewMovieCard(userId, pageable, scoreInteger);
	}

	//取得影評人電影評論
	public Page<ReviewMovieCardDto> getPersonalReviewMovieCardPage(
			String reviewerName, Integer userId, String page) {
		User reviewer = userRepository.findByUsername(reviewerName)
				.orElseThrow(()->new UserNotFoundException("影評人不存在"));
		Integer reviewerId = reviewer.getUserId();
		if (userId != null && !userRepository.existsById(userId)) {
			throw new UserNotFoundException();
		}
		//頁數
		int pageInteger = 1;
		try {
			pageInteger = Integer.parseInt(page);
		}catch(NumberFormatException e) {
			pageInteger = 1;
		}
		Pageable pageable = PageRequest.of(pageInteger - 1, 10);
		return reviewRepository.findPersonalReviewMovieCard(reviewerId, userId, pageable);
	}
	
	@Override
	public List<ReviewDto> toDtoList (List<Review> reviews, Integer userId) {
		List<Object[]> reactions = userId == null ? Collections.emptyList() : reviewReactionRepository.findReactionByUserId(userId);
		Map<Integer, Integer> reactionMap = reactions.stream()
				.collect(Collectors.toMap(row->(Integer)row[0], row->(Integer)row[1]));
		return reviews.stream().map(review->{
			ReviewDto dto = reviewMapper.toDto(review);
			dto.setAuthorId(review.getUser().getUserId());
			dto.setAuthorName(review.getUser().getUsername());
			dto.setAuthorImagePath(review.getUser().getImagePath());
			dto.setLikeCount(calculateLikeCount(review));
			dto.setReaction(reactionMap.getOrDefault(review.getReviewId(),0));
			dto.setCreatedDate(review.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			return dto;
		}).toList();
	}
	
	public Integer calculateLikeCount(Review review) {
		return reviewReactionRepository.countLike(review.getReviewId());
	}

}
