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

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

  @Autowired
  ChatMessageRepository chatMessageRepository;

  @Autowired
  UserRepository userRepository;

  @Override //傳入user、room、content 回傳完整資訊
  public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
    User user = userRepository.findById(chatMessageDto.getUserId())
        .orElseThrow(UserNotFoundException::new);
    ChatMessage chatMessage = toEntity(chatMessageDto, user);
    chatMessageRepository.save(chatMessage);
    return toDto(chatMessage);
  }

  @Override
  public List<ChatMessageDto> getChatMessageByRoomName(String roomName) {
    return chatMessageRepository.getChatMessageByRoomName(roomName);
  }

  private ChatMessage toEntity(ChatMessageDto dto, User user){
    ChatMessage chatMessage = new ChatMessage();
    chatMessage.setUser(user);
    chatMessage.setChatRoomName(dto.getChatRoomName());
    chatMessage.setContent(dto.getContent());
    return chatMessage;
  }

  private ChatMessageDto toDto(ChatMessage chatMessage){
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
