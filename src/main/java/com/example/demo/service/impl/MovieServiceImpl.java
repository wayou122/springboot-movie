package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.movieException.MovieNotFoundException;
import com.example.demo.mapper.MovieMapper;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.model.dto.MovieCardDto;
import com.example.demo.model.dto.MovieDto;
import com.example.demo.model.dto.ReviewDto;
import com.example.demo.model.entity.Movie;
import com.example.demo.repository.MovieRepository;
import com.example.demo.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService{

	@Autowired
	MovieRepository movieRepository;
	@Autowired
	MovieMapper movieMapper;
	@Autowired
	ReviewMapper reviewMapper;
	
	@Override
	public List<MovieCardDto> findAll() {
		List<Movie> movies = movieRepository.findAllWithReviews();
		return movies.stream().map(m->toCardDto(m)).toList();
	}
	
	@Override
	public List<MovieCardDto> findByTitle(String title) {
		List<Movie> movies = movieRepository.findByTitle(title);
		if (movies == null) {
			return null;		
		}
		return movies.stream().map(m->toCardDto(m)).toList();
	}

	@Override
	public List<MovieCardDto> findByType(String type) {
		List<Movie> movies;
		if (type.equals("其他")) {
			movies = movieRepository.findByType("未定");
			movies.addAll(movieRepository.findByType("其他類型"));
		}else {
			movies = movieRepository.findByType(type);
		}
		if (movies == null) {
			return null;
		}
		return movies.stream().map(m->toCardDto(m)).toList();
	}

	@Override
	public MovieDto findById(Integer movieId) {
		Optional<Movie> optMovie =  movieRepository.findByIdWithReviews(movieId);
		if(optMovie.isEmpty()) {
			throw new MovieNotFoundException("查無電影");
		}
		Movie movie = optMovie.get();
		MovieDto movieDto = toDto(movie);
		List<ReviewDto> reviewDtos = movie.getReviews().stream().map(r->reviewMapper.toDto(r)).toList();
		movieDto.setReviews(reviewDtos);
		return movieDto;
	}
	
	@Override
	public void add(MovieDto movieDto) {
		movieRepository.save(movieMapper.toEntity(movieDto));
	}
	
	@Override
	public void addAll(List<MovieDto> movieDtos) {
		List<Movie> movies = movieDtos.stream().map(m->movieMapper.toEntity(m)).toList();
		movieRepository.saveAll(movies);
	}
	
	@Override
	public Integer calculateReviewCount(Movie movie) {
		return movie.getReviews().size();
	}
	
	@Override
	public Double calculateScoreAvg(Movie movie) {
		return movie.getReviews().stream().mapToInt(r->r.getScore()).average().orElse(0.0);
	}
	
	@Override
	public MovieDto toDto(Movie movie) {
		MovieDto dto = movieMapper.toDto(movie);
		dto.setReviewCount(calculateReviewCount(movie));
		dto.setScoreAvg(calculateScoreAvg(movie));
		return dto;
	}
	
	@Override
	public MovieCardDto toCardDto(Movie movie) {
		MovieCardDto dto = movieMapper.toCardDto(movie);
		dto.setReviewCount(calculateReviewCount(movie));
		dto.setScoreAvg(calculateScoreAvg(movie));
		return dto;
	}

}
