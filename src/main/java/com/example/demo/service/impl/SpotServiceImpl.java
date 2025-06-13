package com.example.demo.service.impl;

import com.example.demo.exception.movieException.MovieNotFoundException;
import com.example.demo.exception.userException.UserNotFoundException;
import com.example.demo.mapper.SpotMapper;
import com.example.demo.model.dto.SpotDto;
import com.example.demo.model.entity.Movie;
import com.example.demo.model.entity.Spot;
import com.example.demo.model.entity.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.SpotRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotServiceImpl implements SpotService {

  @Autowired
  SpotRepository spotRepository;
  @Autowired
  MovieRepository movieRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  SpotMapper spotMapper;

  @Override
  public void addSpot(Integer userId, SpotDto spotDto){
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    Movie movie = movieRepository.findById(spotDto.getMovieId())
        .orElseThrow(MovieNotFoundException::new);
    Spot spot = spotMapper.toEntity(spotDto);
    spot.setMovie(movie);
    spot.setCreator(user);
    spotRepository.save(spot);
  }

  @Override
  public List<SpotDto> findAllSpots() {
    List<SpotDto> spots = spotRepository.findAllSpots();
    return spots;
  }
}
