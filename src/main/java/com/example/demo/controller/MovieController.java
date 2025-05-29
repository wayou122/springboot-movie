package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.MovieCardDto;
import com.example.demo.model.dto.MovieDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.MovieService;

@RestController
@RequestMapping("/tiann/movie")
public class MovieController {
	@Autowired
	private MovieService movieService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<MovieCardDto>>> getMovie() {
		List<MovieCardDto> movies = movieService.findAll();
		return ResponseEntity.ok(ApiResponse.success(movies));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<MovieDto>> getSpecificMovie(
			@PathVariable Integer id) {
		MovieDto movie = movieService.findById(id);
		return ResponseEntity.ok(ApiResponse.success(movie));
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<MovieCardDto>>> searchMovie(
			@RequestParam String keyword) {
		List<MovieCardDto> movies = movieService.findByTitle("%"+keyword+"%");
		movies.stream().sorted((m1,m2) -> m1.getReviewCount() - m2.getReviewCount());
		return ResponseEntity.ok(ApiResponse.success(movies));
	}

	@GetMapping("/filter")
	public ResponseEntity<ApiResponse<List<MovieCardDto>>> filterMovie(
			@RequestParam String type) {
		List<MovieCardDto> movies = movieService.findByType(type);
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
