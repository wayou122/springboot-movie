package com.example.demo.model.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.example.demo.model.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MovieDto {
	private Integer movieId;
	private String title;
	@JsonFormat(pattern = "yyyy/MM/dd")
	private LocalDate releaseDate;
	private String summary;
	private String type;
	private Integer length;
	private String posterUrl;
	private String bannerUrl;
	private String director;
	private String actor;
	private String rating;
	private Double scoreAvg;
	private Long reviewCount;
	private List<ReviewDto> reviews;
	private Boolean collected;
}
