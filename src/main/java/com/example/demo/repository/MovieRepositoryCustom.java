package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.dto.MoviesFilterDto;
import com.example.demo.model.entity.Movie;

public interface MovieRepositoryCustom {
	List<Movie> findMoviesByFilter(MoviesFilterDto filter, Integer userId);
}
