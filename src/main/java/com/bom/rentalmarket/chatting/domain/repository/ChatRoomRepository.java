package com.bom.rentalmarket.chatting.domain.repository;

import com.bom.rentalmarket.chatting.domain.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
