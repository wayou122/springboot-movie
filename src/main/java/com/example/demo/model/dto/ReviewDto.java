package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
	private Integer reviewId;
	private String authorName;
	private Integer authorId;
	private String content;
	private Integer score;
	private Integer likeCount;
	private Integer reaction;
	private String createdDate;
}
