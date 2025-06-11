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
			WHERE (:typeFilter IS NULL OR m.type IN :typeFilter)
			AND (:keywordFilter IS NULL OR m.title LIKE %:keywordFilter%)
			""")
	Page<MovieCardDto> findAllMovieCard(
			Integer userId, Pageable pageable, List<String> typeFilter, String keywordFilter);
//					AND (:keyword IS NULL OR m.title LIKE :keyword)
	@Query(value= """
			SELECT
				m.movie_id,
				m.title,
				m.release_date,
				m.type,
				m.length,
				m.poster_url,
				m.banner_url,
				m.director,
				m.actor,
				m.rating,
				COALESCE(AVG(r.score), 0) AS scoreAvg,
			  COUNT(r.review_id) AS reviewCount,
			  CASE WHEN :userId IS NOT NULL AND EXISTS(
			  	SELECT 1 FROM watchlist w WHERE w.movie_id = m.movie_id AND w.user_id = :userId
			  	) THEN true ELSE false
				END
				FROM Movie m
				LEFT JOIN review r ON (r.movie_id =  m.movie_id)
				WHERE (:typeFilter IS NULL OR m.type IN :typeFilter)
				GROUP BY m.movie_id
				ORDER BY
					CASE WHEN :sort = 'score_desc' THEN scoreAvg END DESC,
					CASE WHEN :sort = 'score_asc' THEN scoreAvg END ASC,
					CASE WHEN :sort = 'reviewCount_desc' THEN reviewCount END DESC,
					m.release_date DESC, m.movie_id ASC
			""", countQuery= """
				SELECT COUNT(*) FROM movie m
			  WHERE (:typeFilter IS NULL OR m.type IN (:typeFilter))
			""", nativeQuery = true)
	Page<MovieCardDto> findAllMovieCardsNative(Integer userId, String sort, List<String> typeFilter, String keyword, Pageable pageable);
}
