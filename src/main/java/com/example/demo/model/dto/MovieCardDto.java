package com.example.demo.model.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MovieCardDto {
	private Integer movieId;
	private String title;
	private LocalDate releaseDate;
	private String type;
	private Integer length;
	private String posterUrl;
	private String bannerUrl;
	private String director;
	private String actor;
	private String rating;
	private Double scoreAvg;
	private Integer reviewCount;
	private Boolean collected;
}
