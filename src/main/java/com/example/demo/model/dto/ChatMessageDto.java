package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
  private Long chatMessageId;
  private Integer userId;
  private String username;
  private String userImagePath;
  private String content;
  private String chatRoomName;
  private LocalDateTime createdDate;
}
