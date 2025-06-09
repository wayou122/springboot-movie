package com.example.demo.controller;

import java.util.List;

import com.example.demo.exception.ParamsInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

  // 取得單一電影
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

  // 取得所有電影
  @GetMapping
  public ResponseEntity<ApiResponse<Page<MovieCardDto>>> getAllMovieCardDto(
      HttpSession httpSession,
      @RequestParam(defaultValue = "1") String page,
      @RequestParam(defaultValue = "score_desc") String sort,
      @RequestParam(defaultValue = "all") String type,
      @RequestParam(defaultValue = "") String keyword) {
    //使用者資訊
    Integer userId = null;
    if (httpSession.getAttribute("userCert") != null) {
      userId = ((UserCert) httpSession.getAttribute("userCert")).getUserId();
    }
    // 頁數
    int pageInteger = 1;
    try {
      pageInteger = Integer.parseInt(page);
    } catch (NumberFormatException e) {
      throw new ParamsInvalidException();
    }
    // 排序
    Sort sortOption = switch (sort) {
      case "score_desc" -> Sort.by(Sort.Direction.DESC, "scoreAvg");
      case "score_asc" -> Sort.by(Sort.Direction.ASC, "scoreAvg");
      case "reviewCount_desc" -> Sort.by(Sort.Direction.DESC, "reviewCount");
      case "releaseDate_desc" -> Sort.by(Sort.Direction.DESC, "releaseDate");
      default -> throw new ParamsInvalidException();
    };
    // 分頁查詢
    Pageable pageable = PageRequest.of(pageInteger - 1, 10, sortOption);
    Page<MovieCardDto> dtos = movieService.getMovieCardDtosPage(userId, pageable, type,keyword);
    return ResponseEntity.ok(ApiResponse.success(dtos));
  }

//	@GetMapping
//	public ResponseEntity<ApiResponse<List<MovieCardDto>>> getAllMovies(HttpSession httpSession) {
//		Integer userId = null;
//		if (httpSession.getAttribute("userCert") != null) {
//			userId = ((UserCert) httpSession.getAttribute("userCert")).getUserId();
//		}
//		List<MovieCardDto> movies = movieService.findAll(userId);
//		return ResponseEntity.ok(ApiResponse.success(movies));
//	}


//	@PostMapping
//	public ResponseEntity<ApiResponse<List<MovieCardDto>>> findMoviesByFilter(
//			@ModelAttribute MoviesFilterDto moviesFilterDto, HttpSession httpSession) {
//		Integer userId = null;
//		if (httpSession.getAttribute("userCert") != null) {
//			userId = ((UserCert) httpSession.getAttribute("userCert")).getUserId();
//		}
//		List<MovieCardDto> movies = movieService.findMoviesByFilter(moviesFilterDto, userId);
//		return ResponseEntity.ok(ApiResponse.success(movies));
//	}

//	@PostMapping
//	public ResponseEntity<ApiResponse<List<MovieCardDto>>> getFilteredMovie(
//			@ModelAttribute MoviesFilterDto moviesFilterDto, HttpSession httpSession) {
//		Integer userId = null;
//		if (httpSession.getAttribute("userCert") != null) {
//			userId = ((UserCert) httpSession.getAttribute("userCert")).getUserId();
//		}
//		List<MovieCardDto> movies = movieService.getFilteredMovies(moviesFilterDto, userId);			
//		return ResponseEntity.ok(ApiResponse.success(movies));
//	}

//	@GetMapping("/search")
//	public ResponseEntity<ApiResponse<List<MovieCardDto>>> searchMovie(
//			@RequestParam String keyword, HttpSession httpSession) {
//		Integer userId = null;
//		if (httpSession.getAttribute("userCert") != null) {
//			userId = ((UserCert) httpSession.getAttribute("userCert")).getUserId();
//		}
//		List<MovieCardDto> movies = movieService.findByTitle("%"+keyword+"%", userId);
//		return ResponseEntity.ok(ApiResponse.success(movies));
//	}

  // 新增電影
  @PostMapping("/add")
  public ResponseEntity<ApiResponse<Void>> addMovie(
      @RequestBody MovieDto movieDto) {
    movieService.add(movieDto);
    return ResponseEntity.ok(ApiResponse.success("新增成功"));
  }

  // 新增多部電影
  @PostMapping("/addAll")
  public ResponseEntity<ApiResponse<Void>> addAllMovie(
      @RequestBody List<MovieDto> movieDtos) {
    movieService.addAll(movieDtos);
    return ResponseEntity.ok(ApiResponse.success("新增成功"));
  }

}
