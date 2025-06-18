package com.example.demo.service.impl;

import com.example.demo.exception.userException.UserNotFoundException;
import com.example.demo.mapper.ChatMessageMapper;
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
  @Autowired
  ChatMessageMapper chatMessageMapper;

  @Override //傳入user、room、content 回傳完整資訊
  public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
    User user = userRepository.findById(chatMessageDto.getUserId())
        .orElseThrow(UserNotFoundException::new);
    ChatMessage chatMessage = chatMessageMapper.toEntity(chatMessageDto, user);
    chatMessageRepository.save(chatMessage);
    return chatMessageMapper.toDto(chatMessage);
  }

  @Override
  public List<ChatMessageDto> getChatMessageByRoomName(String roomName) {
    return chatMessageRepository.getChatMessageByRoomName(roomName);
  }
}
