package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.MovieCardView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.dto.MovieDto;
import com.example.demo.model.dto.MoviesFilterDto;
import com.example.demo.model.entity.Movie;
import com.example.demo.model.dto.MovieCardDto;

public interface MovieService {

	List<MovieCardDto> findAll(Integer userId);
	List<MovieCardDto> getFilteredMovies(MoviesFilterDto moviesFilterDto, Integer userId);
	List<MovieCardDto> findByType(String type, Integer userId);
	List<MovieCardDto> findByFilter(MoviesFilterDto moviesFilterDto, Integer userId);
	List<MovieCardDto> findWatchlist(Integer userId);
	List<MovieCardDto> findWatchlistByFilter(MoviesFilterDto moviesFilterDto, Integer userId);
	List<MovieCardDto> findMoviesByFilter(MoviesFilterDto filter, Integer userId);

	MovieDto findById(Integer movieId, Integer userId);
	void add(MovieDto movieDto);
	void addAll(List<MovieDto> movieDtos);
	Long calculateReviewCount(Movie movie);
	Double calculateScoreAvg(Movie movie);
	List<MovieCardDto> toCardDtoList(List<Movie> movies, Integer userId);
	List<MovieDto> toDtoList(List<Movie> movies, Integer userId);
	MovieDto toDto(Movie movie, Integer usrId);
	MovieCardDto toCardDto(Movie movie, Integer usrId);
	Page<MovieCardDto> getMovieCardDtosPage(Integer userId, Pageable pageable, String typeFilter, String keyword);
	Page<MovieCardView> getMoviePage(Integer userId, String page, String sort, String type, String keyword);
}
