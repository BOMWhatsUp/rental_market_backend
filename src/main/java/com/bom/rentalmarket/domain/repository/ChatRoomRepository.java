package com.bom.rentalmarket.domain.repository;

import com.bom.rentalmarket.domain.model.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomName(String roomId);
}
