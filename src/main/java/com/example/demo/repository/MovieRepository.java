package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.dto.MovieCardView;
import com.example.demo.model.dto.MovieTitleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.dto.MovieCardDto;
import com.example.demo.model.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer>{

	// 搜尋單一電影
	@Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE m.movieId = :movieId")
	Optional<Movie> findByIdWithReviews(Integer movieId);

	// 尋找所有電影名稱和id
	@Query("SELECT m.movieId AS movieId, m.title AS title FROM Movie m")
	List<MovieTitleDto> findAllMovieTitleAndId();

	// 搜尋電影標題
	@Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE title LIKE :title")
	Optional<Movie> findByTitle(String title);

	@Query("SELECT m.title FROM Movie m WHERE m.title LIKE :keyword OR m.summary LIKE :keyword")
	List<String> findByKeyword(String keyword);

	//搜尋電影卡
	@Query("""
				SELECT
						m.movieId as movieId,
						m.title as title,
						m.releaseDate as releaseDate,
						m.type as type,
						m.length as length,
						m.posterUrl as posterUrl,
						m.bannerUrl as bannerUrl,
						m.director as director,
						m.actor as actor,
						m.rating as rating,
						COALESCE(AVG(r.score), 0) as scoreAvg,
						COUNT(r.id) as reviewCount,
						CASE WHEN :userId IS NOT NULL AND EXISTS (
						SELECT 1 FROM Movie m2 JOIN m2.watchlistUsers u
									WHERE m2.id = m.id AND u.id = :userId
							) THEN true ELSE false END as collected
				FROM Movie m
				LEFT JOIN Review r ON r.movie = m
				WHERE (:typeFilter IS NULL OR m.type IN :typeFilter)
								AND (:keyword IS NULL OR m.title LIKE :keyword)
				GROUP BY m.movieId
				ORDER BY
								CASE WHEN :sort = 'score_desc' THEN COALESCE(AVG(r.score), 0) END DESC,
								CASE WHEN :sort = 'score_asc' THEN COALESCE(AVG(r.score), 0) END ASC,
								CASE WHEN :sort = 'reviewCount_desc' THEN COUNT(r.id) END DESC,
								m.releaseDate DESC, m.id ASC
	""")
	Page<MovieCardView> findAllMovieCardsWithInterfaceProjection(
			@Param("userId") Integer userId,
			@Param("sort") String sort,
			@Param("typeFilter") List<String> typeFilter,
			@Param("keyword") String keyword,
			Pageable pageable);

	//abandoned 搜尋電影卡，sort排序依照計算欄位會不穩定，order by無法寫case
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

	// abandoned
	@Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE m.movieId IN :moviesId")
	List<Movie> findAllWithReviewsByIds(List<Integer> moviesId);

	@Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE m.type = :type")
	List<Movie> findByType(String type);

	@Query("SELECT m FROM Movie m LEFT JOIN FETCH m.reviews")
	List<Movie> findAllWithReviews();

}
