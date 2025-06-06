package com.example.demo.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.model.dto.MoviesFilterDto;
import com.example.demo.model.entity.Movie;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

public class MovieRepositoryCustomImpl implements MovieRepositoryCustom{

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Movie> findMoviesByFilter(MoviesFilterDto filter, Integer userId) {
		
		StringBuilder jpql = new StringBuilder("SELECT DISTINCT m FROM Movie m ");
		jpql.append("LEFT JOIN FETCH m.reviews r ");
		
		List<String> conditions = new ArrayList<>();
		Map<String, Object> params = new HashMap<>();
		
		if( filter.getWatchlist() && userId!=null) {
			jpql.append("JOIN m.watchlistUsers u ");
			conditions.add("u.userId = :userId");
			params.put("userId", userId);
		}
		
		if (filter.getType()!=null && !filter.getType().equals("全部類型")) {
			if (filter.getType().equals("其他")) {
				conditions.add("m.type IN :otherTypes");
				params.put("otherTypes",List.of("未定","其他類型"));
			}else {
				conditions.add("m.type = :type");
				params.put("type", filter.getType());
			}
		}
		
		if (filter.getKeyword()!=null && !filter.getKeyword().isBlank()) {
			conditions.add("LOWER(m.title) LIKE :keyword");
			params.put("keyword", "%" + filter.getKeyword().toLowerCase() + "%");
		}
		
		if (!conditions.isEmpty()) {
			jpql.append(" WHERE ");
			jpql.append(String.join(" AND ",conditions));
		}
		
		TypedQuery<Movie> query = em.createQuery(jpql.toString(),Movie.class);
		params.forEach(query::setParameter);
		
		return query.getResultList();
	}
	
}
