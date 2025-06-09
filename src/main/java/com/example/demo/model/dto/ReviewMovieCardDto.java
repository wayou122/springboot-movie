package com.example.demo.model.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewMovieCardDto {
	private Integer reviewId;
	private Integer authorId;
	private String createdDate;
	private Integer score;
	private String content;
	private String authorName;
	private String authorImagePath;
	private Integer likeCount;
	private Integer reaction;
	private Integer movieId;
	private String title;
	private String posterUrl;
	
	public ReviewMovieCardDto(
	    Integer reviewId, Integer authorId, LocalDateTime createdDate,
	    Integer score, String content, String authorName, String authorImagePath,
			Long likeCount, Integer reaction, Integer movieId, String title, String posterUrl
	) {
	    this.reviewId = reviewId;
	    this.authorId = authorId;
	    this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	    this.score = score;
	    this.content = content;
	    this.authorName = authorName;
			this.authorImagePath = authorImagePath;
	    this.likeCount = likeCount != null ? likeCount.intValue() : 0;
	    this.reaction = reaction;
	    this.movieId = movieId;
	    this.title = title;
	    this.posterUrl = posterUrl;
	}
}

