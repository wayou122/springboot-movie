package com.example.demo.mapper;

import com.example.demo.model.dto.SpotDto;
import com.example.demo.model.entity.Spot;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpotMapper {
  @Autowired
  private ModelMapper modelMapper;

  public SpotDto toDto (Spot spot){
    SpotDto dto = modelMapper.map(spot, SpotDto.class);
    return dto;
  }
  public Spot toEntity(SpotDto spotDto){
    return modelMapper.map(spotDto, Spot.class);
  }

}
