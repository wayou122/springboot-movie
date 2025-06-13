package com.example.demo.controller;

import com.example.demo.exception.userException.UserException;
import com.example.demo.model.dto.SpotDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.SpotService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tiann/spot")
public class SpotController {
  @Autowired
  private SpotService spotService;

  //取得所有景點
  @GetMapping
  public ResponseEntity<ApiResponse<List<SpotDto>>> getAllSpots(){
    List<SpotDto> spots = spotService.findAllSpots();
    return ResponseEntity.ok(ApiResponse.success("查詢成功",spots));
  }

  //新增景點
  @PostMapping
  public ResponseEntity<ApiResponse<Void>> addSpot(
      @RequestBody SpotDto spotDto, HttpSession httpSession){
    if (httpSession.getAttribute("userCert")==null){
      throw new UserException("尚未登入");
    }
    UserCert userCert = (UserCert) httpSession.getAttribute("userCert");
    Integer userId = userCert.getUserId();
    spotService.addSpot(userId, spotDto);
    return ResponseEntity.ok(ApiResponse.success("新增成功"));
  }
}
