package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.MovieCardDto;
import com.example.demo.model.dto.MovieDto;
import com.example.demo.model.dto.MoviesFilterDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.MovieService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/tiann/movie")
public class MovieController {
	@Autowired
	private MovieService movieService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<MovieCardDto>>> getAllMovies(HttpSession httpSession) {
		Integer userId = null;
		if (httpSession.getAttribute("userCert") != null) {
			userId = ((UserCert) httpSession.getAttribute("userCert")).getUserId();
		}
		List<MovieCardDto> movies = movieService.findAll(userId);
		return ResponseEntity.ok(ApiResponse.success(movies));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<List<MovieCardDto>>> getFilteredMovie(
			@ModelAttribute MoviesFilterDto moviesFilterDto, HttpSession httpSession) {
		Integer userId = null;
		if (httpSession.getAttribute("userCert") != null) {
			userId = ((UserCert) httpSession.getAttribute("userCert")).getUserId();
		}
		List<MovieCardDto> movies = movieService.getFilteredMovies(moviesFilterDto, userId);			
		return ResponseEntity.ok(ApiResponse.success(movies));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<MovieDto>> getSpecificMovie(
			@PathVariable Integer id, HttpSession httpSession) {
		Integer userId = null;
		if (httpSession.getAttribute("userCert") != null) {
			userId = ((UserCert) httpSession.getAttribute("userCert")).getUserId();
		}
		MovieDto movie = movieService.findById(id, userId);
		return ResponseEntity.ok(ApiResponse.success(movie));
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<MovieCardDto>>> searchMovie(
			@RequestParam String keyword, HttpSession httpSession) {
		Integer userId = null;
		if (httpSession.getAttribute("userCert") != null) {
			userId = ((UserCert) httpSession.getAttribute("userCert")).getUserId();
		}
		List<MovieCardDto> movies = movieService.findByTitle("%"+keyword+"%", userId);
		movies.stream().sorted((m1,m2) -> m1.getReviewCount() - m2.getReviewCount());
		return ResponseEntity.ok(ApiResponse.success(movies));
	}
	
	@PostMapping("/add")
	public ResponseEntity<ApiResponse<Void>> addMovie(
			@RequestBody MovieDto movieDto){
		movieService.add(movieDto);
		return ResponseEntity.ok(ApiResponse.success("新增成功"));
	}
	
	@PostMapping("/addAll")
	public ResponseEntity<ApiResponse<Void>> addAllMovie(
			@RequestBody List<MovieDto> movieDtos){
		movieService.addAll(movieDtos);
		return ResponseEntity.ok(ApiResponse.success("新增成功"));
	}
}
