package com.example.demo.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ParamsInvalidException;
import com.example.demo.exception.movieException.MovieNotFoundException;
import com.example.demo.exception.userException.UserNotFoundException;
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
import com.example.demo.service.ReviewService;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	MovieRepository movieRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ReviewService reviewService;

	@Autowired
	MovieMapper movieMapper;
	@Autowired
	ReviewMapper reviewMapper;

	private	Map<String,String> typeMap = 
			Map.of("all","全部","drama","劇情片","documentary","紀錄片","animation","動畫","short","短片","other","其他");

	@Override
	public List<MovieCardDto> findAll(Integer userId) {
		List<Movie> movies = movieRepository.findAllWithReviews();
		return toCardDtoList(movies, userId);
	}

	@Override
	public MovieDto findById(Integer movieId, Integer userId) {
		Movie movie = movieRepository.findByIdWithReviews(movieId)
				.orElseThrow(()->new MovieNotFoundException("查無此電影"));
		MovieDto movieDto = toDto(movie, userId);
		List<ReviewDto> reviewDtos = reviewService.toDtoList(movie.getReviews(), userId);
		movieDto.setReviews(reviewDtos);
		return movieDto;
	}

	// abandoned
	@Override
	public List<MovieCardDto> findMoviesByFilter(MoviesFilterDto filter, Integer userId) {
		List<Movie> movies = movieRepository.findMoviesByFilter(filter, userId);
		if (movies == null) {
			return null;
		}
		List<MovieCardDto> dtos = toCardDtoList(movies, userId);
		return sortMovies(dtos, filter.getSort());
	}

	// abandoned
	List<MovieCardDto> sortMovies(List<MovieCardDto> movies, String sort) {
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
			return movies.stream().sorted(Comparator.comparingLong(MovieCardDto::getReviewCount).reversed()).toList();
		}
		return movies;
	}

	// abandoned
	@Override
	public List<MovieCardDto> getFilteredMovies(MoviesFilterDto moviesFilterDto, Integer userId) {
		if (moviesFilterDto.getWatchlist()) {
			return findWatchlistByFilter(moviesFilterDto, userId);
		} else {
			return findByFilter(moviesFilterDto, userId);
		}
	}

	// abandoned
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
		return sortMovies(movies, sort);
	}

	// abandoned
	@Override
	public List<MovieCardDto> findByFilter(MoviesFilterDto moviesFilterDto, Integer userId) {
		String sort = moviesFilterDto.getSort();
		String type = moviesFilterDto.getType();
		List<MovieCardDto> movies = findByType(type, userId);
		if (movies == null)
			return null;
		if (movies.size() <= 1)
			return movies;
		return sortMovies(movies, sort);
	}

//abandoned
	@Override
	public List<MovieCardDto> findWatchlist(Integer userId) {
		List<Integer> movieIds = userRepository.findWatchlistMovieIds(userId);
		List<Movie> movies = movieRepository.findAllWithReviewsByIds(movieIds);
		return toCardDtoList(movies, userId);
	}

//abandoned
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
	public void add(MovieDto movieDto) {
		movieRepository.save(movieMapper.toEntity(movieDto));
	}

	@Override
	public void addAll(List<MovieDto> movieDtos) {
		List<Movie> movies = movieDtos.stream().map(m -> movieMapper.toEntity(m)).toList();
		movieRepository.saveAll(movies);
	}

	@Override
	public Long calculateReviewCount(Movie movie) {
		return Long.valueOf( movie.getReviews().size());
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

	@Override
  public Page<MovieCardDto> getMovieCardDtosPage(
      Integer userId, Pageable pageable, String typeFilter, String keyword) {
		if (userId != null && !userRepository.existsById(userId)) {
			throw new UserNotFoundException();
		}
		List<String> typeFilterList; 
		if (!typeMap.containsKey(typeFilter)) {
			throw new ParamsInvalidException();
		}else if(typeFilter.equalsIgnoreCase("all")){
			typeFilterList = null;
		}else if (typeFilter.equals("other")) {
			typeFilterList = List.of("未定","其他類型");
		}else {
			typeFilterList = List.of(typeMap.get(typeFilter));
		}
    String keywordFilter = null;
    if (keyword != null && !keyword.isBlank()){
      keywordFilter = "%"+keyword+"%";
    }
		return movieRepository.findAllMovieCard(userId, pageable, typeFilterList, keywordFilter);
	}
}
