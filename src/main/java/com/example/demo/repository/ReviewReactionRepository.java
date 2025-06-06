package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.ReviewReaction;
import com.example.demo.model.entity.ReviewReactionId;

@Repository
public interface ReviewReactionRepository extends JpaRepository<ReviewReaction, ReviewReactionId>{

	@Query(value = "SELECT review_id, reaction FROM review_reaction WHERE user_id = :userId", nativeQuery = true)
	List<Object[]> findReactionByUserId(Integer userId);
	
	@Query(value = "SELECT COUNT(*) FROM review_reaction WHERE review_id = :reviewId AND reaction=1", nativeQuery = true)
	Integer countLike (Integer reviewId);
}
