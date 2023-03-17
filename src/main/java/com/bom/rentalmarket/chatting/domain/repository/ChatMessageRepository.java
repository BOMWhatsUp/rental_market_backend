package com.bom.rentalmarket.chatting.domain.repository;

import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    ChatMessage findFirstByChatRoom_IdOrderBySendTimeDesc(Long id);
}