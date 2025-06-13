package com.example.demo.repository;

import com.example.demo.model.dto.SpotDto;
import com.example.demo.model.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Integer> {

  @Query("""
    SELECT new com.example.demo.model.dto.SpotDto(
      s.spotId,
      s.spotName,
      s.description,
      s.lat,
      s.lng,
      s.movie.movieId,
      s.movie.title,
      s.creator.userId,
      s.creator.username
    )
    FROM Spot s
    """)
  List<SpotDto> findAllSpots();
}
