package com.example.demo.model.dto;

import java.time.LocalDate;

public interface MovieCardView {
	Integer getMovieId();
	String getTitle();
	LocalDate getReleaseDate();
	String getType();
	Integer getLength();
	String getPosterUrl();
	String getBannerUrl();
	String getDirector();
	String getActor();
	String getRating();
	Double getScoreAvg();
	Long getReviewCount();
	Boolean getCollected();
}