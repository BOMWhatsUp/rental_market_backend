package com.bom.rentalmarket.chatting.domain.repository;

import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import com.bom.rentalmarket.chatting.domain.model.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<ChatMessage> findFirstByChatRoom_IdOrderBySendTimeDesc(Long id);
    Optional<ChatMessage> findTop1ByChatRoomAndNicknameOrderById(ChatRoom chatRoom, String nickname);
    void deleteAllByChatRoom_Id(Long roomId);
}