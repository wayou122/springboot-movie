package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.dto.MovieCardDto;
import com.example.demo.model.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer>, MovieRepositoryCustom{
	
	@Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE m.movieId IN :moviesId")
	List<Movie> findAllWithReviewsByIds(List<Integer> moviesId);
	
	@Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE m.type = :type")
	List<Movie> findByType(String type);
	
	@Query("SELECT m FROM Movie m LEFT JOIN FETCH m.reviews")
	List<Movie> findAllWithReviews();
	
	@Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE m.movieId = :movieId")
	Optional<Movie> findByIdWithReviews(Integer movieId);
	
	//電影卡
	@Query("""
			SELECT new com.example.demo.model.dto.MovieCardDto(
				m.movieId,
				m.title,
				m.releaseDate,
				m.type,
				m.length,
				m.posterUrl,
				m.bannerUrl,
				m.director,
				m.actor,
				m.rating,
				(SELECT AVG(r.score) FROM Review r WHERE r.movie = m) AS scoreAvg,
				(SELECT COUNT(r) FROM Review r WHERE r.movie = m) AS reviewCount,
				CASE WHEN :userId IS NOT NULL THEN
					(SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
			 		 FROM User u JOIN u.watchlist w WHERE w=m AND u.userId = :userId)
			 			 ELSE false END
			)
			FROM Movie m
			LEFT JOIN m.reviews r
			LEFT JOIN m.watchlistUsers u
			WHERE (:typeFilter IS NULL OR m.type IN :typeFilter)
			AND (:keywordFilter IS NULL OR m.title LIKE :keywordFilter)
			""")
	Page<MovieCardDto> findAllMovieCard(
			Integer userId, Pageable pageable, List<String> typeFilter, String keywordFilter);
	
}
