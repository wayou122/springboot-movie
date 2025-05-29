package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ReviewReaction {
	@EmbeddedId
  private ReviewReactionId id;
	
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@MapsId("reviewId")
	@JoinColumn(name="review_id")
	private Review review;
	
	@Column
	private Integer reaction = 0;
	
}
