package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.entity.Movie;

public interface MovieService {

	MovieDto findById(Integer movieId, Integer userId);
	Page<MovieCardView> getMoviePage(Integer userId, String page, String sort, String type, String keyword);

	void add(MovieDto movieDto);
	void addAll(List<MovieDto> movieDtos);

	List<MovieTitleDto> findAllMovieTitle();

	List<MovieCardDto> findAll(Integer userId);
	List<MovieCardDto> getFilteredMovies(MoviesFilterDto moviesFilterDto, Integer userId);
	List<MovieCardDto> findByType(String type, Integer userId);
	List<MovieCardDto> findByFilter(MoviesFilterDto moviesFilterDto, Integer userId);
	List<MovieCardDto> findWatchlist(Integer userId);

	List<MovieCardDto> findWatchlistByFilter(MoviesFilterDto moviesFilterDto, Integer userId);

	Long calculateReviewCount(Movie movie);
	Double calculateScoreAvg(Movie movie);
	List<MovieCardDto> toCardDtoList(List<Movie> movies, Integer userId);
	List<MovieDto> toDtoList(List<Movie> movies, Integer userId);
	MovieDto toDto(Movie movie, Integer usrId);
	MovieCardDto toCardDto(Movie movie, Integer usrId);
}
