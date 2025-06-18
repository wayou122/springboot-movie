package com.example.demo.mapper;

import com.example.demo.model.dto.ChatMessageDto;
import com.example.demo.model.entity.ChatMessage;
import com.example.demo.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {
  public ChatMessage toEntity(ChatMessageDto dto, User user){
    ChatMessage chatMessage = new ChatMessage();
    chatMessage.setUser(user);
    chatMessage.setChatRoomName(dto.getChatRoomName());
    chatMessage.setContent(dto.getContent());
    return chatMessage;
  }

  public ChatMessageDto toDto(ChatMessage chatMessage){
    ChatMessageDto dto = new ChatMessageDto();
    dto.setUserId(chatMessage.getUser().getUserId());
    dto.setUsername(chatMessage.getUser().getUsername());
    dto.setUserImagePath(chatMessage.getUser().getImagePath());
    dto.setCreatedDate(chatMessage.getCreatedDate());
    dto.setChatRoomName(chatMessage.getChatRoomName());
    dto.setContent(chatMessage.getContent());
    return dto;
  }
}
