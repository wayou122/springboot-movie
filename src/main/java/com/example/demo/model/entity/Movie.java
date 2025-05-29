package com.example.demo.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer movieId;
	
	@Column(nullable = false)
	private String title;
	
	@Column
	private LocalDate releaseDate;
	
	@Column(columnDefinition = "TEXT")
	private String summary;
	
	@Column
	private String type;
	
	@Column
	private Integer length;
	
	@Column
	private String bannerUrl;
	
	@Column
	private String posterUrl;
	
	@Column
	private String director;
	
	@Column
	private String actor;
	
	@Column
	private String rating;
	
	@ManyToMany(mappedBy = "watchlist")
	private List<User> watchlistUsers;	
	
	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime createdDate;
	
	@Column(nullable = false)
	@LastModifiedDate
	private LocalDateTime modifiedDate;
	
	@OneToMany(mappedBy = "movie")
	private List<Review> reviews;
	
	@Override
	public boolean equals(Object o) {
		if (this==o) return true;
		if (!(o instanceof Movie)) return false;
		Movie other = (Movie) o;
		return movieId != null && movieId.equals(other.getMovieId());
	}
	
}
