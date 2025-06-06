package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoviesFilterDto {
	private String sort;
	private String type;
	private String keyword;
	private Boolean watchlist;
}
