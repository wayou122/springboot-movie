package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.MovieDto;
import com.example.demo.model.entity.Movie;
import com.example.demo.model.dto.MovieCardDto;

public interface MovieService {
	List<MovieCardDto> findAll();
	List<MovieCardDto> findByTitle(String title);
	List<MovieCardDto> findByType(String type);
	MovieDto findById(Integer movieId);
	void add(MovieDto movieDto);
	void addAll(List<MovieDto> movieDtos);
	Integer calculateReviewCount(Movie movie);
	Double calculateScoreAvg(Movie movie);
	MovieDto toDto(Movie movie);
	MovieCardDto toCardDto(Movie movie);

}
