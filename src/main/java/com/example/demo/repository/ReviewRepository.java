package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.dto.ReviewMovieCardDto;
import com.example.demo.model.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
	@Query("SELECT CASE WHEN COUNT(r) >0 THEN TRUE ELSE FALSE END "+
	"FROM Review r WHERE r.user.userId = :userId AND r.movie.movieId = :movieId")
	boolean existsByUserIdAndMovieId(Integer userId, Integer movieId);

	//電影評論卡
	@Query("""
			SELECT new com.example.demo.model.dto.ReviewMovieCardDto(
				r.reviewId,
				r.user.userId,
				r.createdDate,
				r.score,
				r.content,
				r.user.username,
				(SELECT COUNT(rr) FROM ReviewReaction rr WHERE rr.review = r AND rr.reaction = 1) AS likeCount,
				CASE WHEN :userId IS NOT NULL THEN (SELECT rr.reaction FROM ReviewReaction rr WHERE rr.review = r AND rr.user.userId = :userId) ELSE 0 END,
				r.movie.movieId,
				r.movie.title,
				r.movie.posterUrl
			)
			FROM Review r
			LEFT JOIN r.user u
			LEFT JOIN r.movie m
			WHERE (:scoreFilter IS NULL OR r.score = :scoreFilter)
			""")
	Page<ReviewMovieCardDto> findAllReviewMovieCard(
			Integer userId, Pageable pageable, Integer scoreFilter);
}
