package com.example.demo.controller;

import com.example.demo.model.dto.ChatMessageDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChatController {
  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;
  @Autowired
  private ChatMessageService chatMessageService;

  @MessageMapping("/chatRoom/{roomName}") //接受前端送到/app/chatRoom/{roomName}的訊息
  // chatMessageDto必須傳入userId, content, roomName
  public void getAndSendMessage(@DestinationVariable String roomName, ChatMessageDto chatMessageDto){
    ChatMessageDto responseDto = chatMessageService.saveMessage(chatMessageDto);
    //發送訊息到/topic/messages/{roomName}
    simpMessagingTemplate.convertAndSend("/topic/messages/"+roomName, responseDto);
  }

  @GetMapping("/chat/{chatRoom}")
  public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getMessage(@PathVariable String chatRoom){
    List<ChatMessageDto> chatMessageDtos = chatMessageService.getChatMessageByRoomName(chatRoom);
    return ResponseEntity.ok(ApiResponse.success(chatMessageDtos));
  }
}
