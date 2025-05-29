package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.ReviewDto;
import com.example.demo.model.entity.Review;

@Component
public class ReviewMapper {
	@Autowired
	private ModelMapper modelMapper;
	
	public ReviewDto toDto(Review review) {
		ReviewDto dto = modelMapper.map(review, ReviewDto.class);
		return dto;
	}
	public Review toEntity(ReviewDto reviewDto) {
		return modelMapper.map(reviewDto, Review.class);
	}
	
}
