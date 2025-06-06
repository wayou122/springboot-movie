package com.example.demo.model.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewReactionId implements Serializable {
	private Integer userId;
	private Integer reviewId;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ReviewReactionId))
			return false;
		ReviewReactionId other = (ReviewReactionId) o;
		return Objects.equals(userId, other.getUserId()) && Objects.equals(reviewId, other.getReviewId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, reviewId);
	}
}
