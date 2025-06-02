package com.example.demo.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.movieException.MovieNotFoundException;
import com.example.demo.mapper.MovieMapper;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.model.dto.MovieCardDto;
import com.example.demo.model.dto.MovieDto;
import com.example.demo.model.dto.MoviesFilterDto;
import com.example.demo.model.dto.ReviewDto;
import com.example.demo.model.entity.Movie;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	MovieRepository movieRepository;
	@Autowired
	UserRepository userRepository;

	@Autowired
	MovieMapper movieMapper;
	@Autowired
	ReviewMapper reviewMapper;

	@Override
	public List<MovieCardDto> findAll(Integer userId) {
		List<Movie> movies = movieRepository.findAllWithReviews();
		return toCardDtoList(movies, userId);
	}

	@Override
	public List<MovieCardDto> findByTitle(String title, Integer userId) {
		List<Movie> movies = movieRepository.findByTitle(title);
		if (movies == null) {
			return null;
		}
		return toCardDtoList(movies, userId);
	}
	@Override
	public List<MovieCardDto> getFilteredMovies(MoviesFilterDto moviesFilterDto, Integer userId) {
		if (moviesFilterDto.getWatchlist()) {
			return findWatchlistByFilter(moviesFilterDto, userId);
		} else {
			return findByFilter(moviesFilterDto, userId);
		}
	}
	@Override
	public List<MovieCardDto> findWatchlistByFilter(MoviesFilterDto moviesFilterDto, Integer userId) {
		List<MovieCardDto> movies = findWatchlist(userId);
		if (movies == null)
			return null;
		String type = moviesFilterDto.getType();
		String sort = moviesFilterDto.getSort();

		if (type.equals("全部類型")) {

		} else if (type.equals("其他")) {
			movies = movies.stream().filter(movie -> movie.getType().equals("未定") || movie.getType().equals("其他類型")).toList();
		} else {
			movies = movies.stream().filter(movie -> movie.getType().equals(type)).toList();
		}
		switch (sort) {
		case "最新上映":
			return movies
					.stream().sorted(Comparator
							.comparing(MovieCardDto::getReleaseDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
					.toList();
		case "評價最高":
			return movies.stream().sorted(Comparator.comparingDouble(MovieCardDto::getScoreAvg).reversed()).toList();
		case "評價最低":
			return movies.stream().sorted(Comparator.comparingDouble(MovieCardDto::getScoreAvg)).toList();
		case "最多評論":
			return movies.stream().sorted(Comparator.comparingInt(MovieCardDto::getReviewCount).reversed()).toList();
		}
		return movies;
	}

	@Override
	public List<MovieCardDto> findByFilter(MoviesFilterDto moviesFilterDto, Integer userId) {
		String sort = moviesFilterDto.getSort();
		String type = moviesFilterDto.getType();
		List<MovieCardDto> movies = findByType(type, userId);
		if (movies == null)
			return null;
		if (movies.size() <= 1)
			return movies;
		switch (sort) {
		case "最新上映":
			return movies
					.stream().sorted(Comparator
							.comparing(MovieCardDto::getReleaseDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
					.toList();
		case "評價最高":
			return movies.stream().sorted(Comparator.comparingDouble(MovieCardDto::getScoreAvg).reversed()).toList();
		case "評價最低":
			return movies.stream().sorted(Comparator.comparingDouble(MovieCardDto::getScoreAvg)).toList();
		case "最多評論":
			return movies.stream().sorted(Comparator.comparingInt(MovieCardDto::getReviewCount).reversed()).toList();
		}
		return movies;
	}
	@Override
	public List<MovieCardDto> findWatchlist(Integer userId) {
		List<Integer> movieIds = userRepository.findWatchlistMovieIds(userId);
		List<Movie> movies = movieRepository.findAllWithReviewsByIds(movieIds);
		return toCardDtoList(movies, userId);
	}

	@Override
	public List<MovieCardDto> findByType(String type, Integer userId) {
		List<Movie> movies;
		if (type.equals("全部類型")) {
			movies = movieRepository.findAllWithReviews();
		} else if (type.equals("其他")) {
			movies = movieRepository.findByType("未定");
			movies.addAll(movieRepository.findByType("其他類型"));
		} else {
			movies = movieRepository.findByType(type);
		}
		if (movies == null) {
			return null;
		}
		return toCardDtoList(movies, userId);
	}

	@Override
	public MovieDto findById(Integer movieId, Integer userId) {
		Optional<Movie> optMovie = movieRepository.findByIdWithReviews(movieId);
		if (optMovie.isEmpty()) {
			throw new MovieNotFoundException("查無電影");
		}
		Movie movie = optMovie.get();
		MovieDto movieDto = toDto(movie, userId);
		List<ReviewDto> reviewDtos = movie.getReviews().stream().map(r -> reviewMapper.toDto(r)).toList();
		movieDto.setReviews(reviewDtos);
		return movieDto;
	}

	@Override
	public void add(MovieDto movieDto) {
		movieRepository.save(movieMapper.toEntity(movieDto));
	}

	@Override
	public void addAll(List<MovieDto> movieDtos) {
		List<Movie> movies = movieDtos.stream().map(m -> movieMapper.toEntity(m)).toList();
		movieRepository.saveAll(movies);
	}

	@Override
	public Integer calculateReviewCount(Movie movie) {
		return movie.getReviews().size();
	}

	@Override
	public Double calculateScoreAvg(Movie movie) {
		return movie.getReviews().stream().mapToInt(r -> r.getScore()).average().orElse(0.0);
	}

	@Override
	public List<MovieDto> toDtoList(List<Movie> movies, Integer userId) {
		List<Integer> watchlist = userId == null ? Collections.emptyList() : userRepository.findWatchlistMovieIds(userId);
		return movies.stream().map(movie -> {
			MovieDto dto = movieMapper.toDto(movie);
			dto.setReviewCount(calculateReviewCount(movie));
			dto.setScoreAvg(calculateScoreAvg(movie));
			dto.setCollected(watchlist.contains(movie.getMovieId()));
			return dto;
		}).toList();
	}

	@Override
	public MovieDto toDto(Movie movie, Integer userId) {
		List<Integer> watchlist = userId == null ? Collections.emptyList() : userRepository.findWatchlistMovieIds(userId);
		MovieDto dto = movieMapper.toDto(movie);
		dto.setReviewCount(calculateReviewCount(movie));
		dto.setScoreAvg(calculateScoreAvg(movie));
		dto.setCollected(watchlist.contains(movie.getMovieId()));
		return dto;
	}

	@Override
	public List<MovieCardDto> toCardDtoList(List<Movie> movies, Integer userId) {
		List<Integer> watchlist = userId == null ? Collections.emptyList() : userRepository.findWatchlistMovieIds(userId);
		return movies.stream().map(m -> {
			MovieCardDto dto = movieMapper.toCardDto(m);
			dto.setReviewCount(calculateReviewCount(m));
			dto.setScoreAvg(calculateScoreAvg(m));
			dto.setCollected(watchlist.contains(m.getMovieId()));
			return dto;
		}).toList();
	}

	@Override
	public MovieCardDto toCardDto(Movie movie, Integer userId) {
		List<Integer> watchlist = userId == null ? Collections.emptyList() : userRepository.findWatchlistMovieIds(userId);
		MovieCardDto dto = movieMapper.toCardDto(movie);
		dto.setReviewCount(calculateReviewCount(movie));
		dto.setScoreAvg(calculateScoreAvg(movie));
		dto.setCollected(watchlist.contains(movie.getMovieId()));
		return dto;
	}
}
