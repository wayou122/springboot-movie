package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class SpotDto {
  private Integer spotId;
  private String spotName;
  private String description;
  private BigDecimal lat;
  private BigDecimal lng;
  private Integer movieId;
  private String movieTitle;
  private Integer creatorId;
  private String creatorName;

  public SpotDto(
      Integer spotId,
      String spotName,
      String description,
      BigDecimal lat,
      BigDecimal lng,
      Integer movieId,
      String movieTitle,
      Integer creatorId,
      String creatorName
  ) {
    this.spotId = spotId;
    this.spotName = spotName;
    this.description = description;
    this.lat = lat;
    this.lng = lng;
    this.movieId = movieId;
    this.movieTitle = movieTitle;
    this.creatorId = creatorId;
    this.creatorName = creatorName;
  }
}
