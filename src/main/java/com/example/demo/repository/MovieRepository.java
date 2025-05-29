package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
	
	@Query("SELECT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE m.movieId IN :moviesId")
	List<Movie> findAllWithReviewsByIds(List<Integer> moviesId);
	
	@Query("SELECT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE m.title LIKE :keyword")
	List<Movie> findByTitle(String keyword);
	
	@Query("SELECT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE m.type = :type")
	List<Movie> findByType(String type);
	
	@Query("SELECT m FROM Movie m LEFT JOIN FETCH m.reviews")
	List<Movie> findAllWithReviews();
	
	@Query("SELECT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE m.movieId = :movieId")
	Optional<Movie> findByIdWithReviews(Integer movieId);
	
	//@Query("SELECT new com.example.dto.MovieReviewSummary(m.movieId, AVG(r.score), COUNT(r)) " +
	//"FROM Movie m LEFT JOIN m.reviews r WHERE m.movieId = :movieId GROUP BY m.movieId")
	//MovieReviewSummary findMovieReviewSummaryByMovieId(@Param("movieId") Integer movieId);
}
