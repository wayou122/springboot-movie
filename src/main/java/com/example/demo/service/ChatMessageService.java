package com.example.demo.service;

import com.example.demo.model.dto.ChatMessageDto;

import java.util.List;

public interface ChatMessageService {
  ChatMessageDto saveMessage(ChatMessageDto chatMessageDto);
  List<ChatMessageDto> getChatMessageByRoomName(String roomName);
}
