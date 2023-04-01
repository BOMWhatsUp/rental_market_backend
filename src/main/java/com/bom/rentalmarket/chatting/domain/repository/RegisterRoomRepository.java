package com.bom.rentalmarket.chatting.domain.repository;

import com.bom.rentalmarket.chatting.domain.model.RegisterRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRoomRepository extends JpaRepository<RegisterRoom, Long> {
    List<RegisterRoom> findByChatRoom_Id(Long id);
    List<RegisterRoom> findAllByNickname(String userName);
}
