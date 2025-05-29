package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.MovieCardDto;
import com.example.demo.model.dto.MovieDto;
import com.example.demo.model.entity.Movie;
import com.example.demo.service.MovieService;

@Component
public class MovieMapper {
	@Autowired
	private ModelMapper modelMapper;
	
	public MovieDto toDto(Movie movie) {
		MovieDto dto = modelMapper.map(movie, MovieDto.class);
		return dto;
	}
	public MovieCardDto toCardDto(Movie movie) {
		MovieCardDto dto = modelMapper.map(movie, MovieCardDto.class);
		return dto;
	}
	public Movie toEntity(MovieDto movieDto) {
		return modelMapper.map(movieDto, Movie.class);
	}
	
}
