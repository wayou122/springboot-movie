package com.example.demo.service.impl;

import com.example.demo.exception.userException.UserNotFoundException;
import com.example.demo.model.dto.ChatMessageDto;
import com.example.demo.model.entity.ChatMessage;
import com.example.demo.model.entity.User;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

  @Autowired
  ChatMessageRepository chatMessageRepository;

  @Autowired
  UserRepository userRepository;

  @Override
  public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
    User user = userRepository.findById(chatMessageDto.getUserId())
        .orElseThrow(UserNotFoundException::new);
    ChatMessage chatMessage = new ChatMessage();
    chatMessage.setUser(user);
    chatMessage.setChatRoomName(chatMessageDto.getChatRoomName());
    chatMessage.setContent(chatMessageDto.getContent());
    chatMessageRepository.save(chatMessage);
    ChatMessageDto completed = new ChatMessageDto();
    completed.setUserId(user.getUserId());
    completed.setUsername(user.getUsername());
    completed.setUserImagePath(user.getImagePath());
    completed.setCreatedDate(LocalDateTime.now());
    completed.setChatRoomName(chatMessage.getChatRoomName());
    completed.setContent(chatMessage.getContent());
    return completed;
  }

  @Override
  public List<ChatMessageDto> getChatMessageByRoomName(String roomName) {
    return chatMessageRepository.getChatMessageByRoomName(roomName);
  }
}
