package com.example.demo.repository;

import com.example.demo.model.dto.ChatMessageDto;
import com.example.demo.model.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  @Query("""
      SELECT new com.example.demo.model.dto.ChatMessageDto(
        c.chatMessageId,
        c.user.userId,
        c.user.username,
        c.user.imagePath,
        c.content,
        c.chatRoomName,
        c.createdDate
      )
      FROM ChatMessage c
      LEFT JOIN c.user u
      WHERE c.chatRoomName = :chatRoomName
      ORDER BY c.createdDate DESC
      LIMIT 30
      """)
  List<ChatMessageDto> getChatMessageByRoomName(String chatRoomName);
}
