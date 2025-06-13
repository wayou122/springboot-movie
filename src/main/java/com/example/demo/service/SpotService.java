package com.example.demo.service;

import com.example.demo.model.dto.SpotDto;

import java.util.List;

public interface SpotService {
  List<SpotDto> findAllSpots();
  void addSpot(Integer userId, SpotDto spotDto);
}
