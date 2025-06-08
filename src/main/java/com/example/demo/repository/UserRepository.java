package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Movie;
import com.example.demo.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);

	@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.watchlist WHERE u.userId = :userId")
	Optional<User> findByIdWithWatchlist(Integer userId);
	
	@Query("SELECT w.movieId FROM User u JOIN u.watchlist w WHERE u.userId = :userId")
	List<Integer> findWatchlistMovieIds(Integer userId);
	
//	@Query("SELECT m.movieId FROM Movie m JOIN m.watchlistUsers u WHERE u.userId = :userId")
//	List<Integer> findWatchListMoveIdById(Integer userId);
}
