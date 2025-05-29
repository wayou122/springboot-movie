package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
	@Query("SELECT CASE WHEN COUNT(r) >0 THEN TRUE ELSE FALSE END "+
	"FROM Review r WHERE r.user.userId = :userId AND r.movie.movieId = :movieId")
	boolean existsByUserIdAndMovieId(Integer userId, Integer movieId);

}
