package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Spot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Integer spotId;

  @Column
  private String spotName;

  @Column
  private String description;

  //經緯度設定小數點後5位
  @Column(precision = 10, scale = 5)
  private BigDecimal lat;

  @Column(precision = 10, scale = 5)
  private BigDecimal lng;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="movie_id")
  private Movie movie;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User creator;

  @Column(nullable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(nullable = false)
  @LastModifiedDate
  private LocalDateTime modifiedDate;
}
