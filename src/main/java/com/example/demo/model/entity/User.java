package com.example.demo.model.entity;

import java.time.LocalDateTime;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer userId;
	
	@Column(unique = true, nullable = false)
	private String username;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	@Column
	private Boolean emailVerf = false;
	
	@Column(nullable=false)
	private String passwordHash;
	
	@Column(nullable = false)
	private String salt;
	
	@Column
	private String role = "user";
	
	@OneToMany(mappedBy = "user")
	private List<Review> reviews;
	
	@ManyToMany
	@JoinTable(name="watchlist",
	joinColumns = @JoinColumn(name="user_id"),
	inverseJoinColumns = @JoinColumn(name="movie_id"))
	private List<Movie> watchlist;
	
	@OneToMany(mappedBy = "user")
	private List<ReviewReaction> likes;
	
	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime createdDate;
	
	@Column(nullable = false)
	@LastModifiedDate
	private LocalDateTime modifiedDate;
	
	@Column
	private String emailVerfToken;

	@Column
	private String passwordToken;
}
