package com.bom.rentalmarket.service;

import com.bom.rentalmarket.domain.chat.ChatMessageForm;
import com.bom.rentalmarket.domain.model.ChatMessage;
import com.bom.rentalmarket.domain.model.ChatRoom;
import com.bom.rentalmarket.domain.repository.ChatMessageRepository;
import com.bom.rentalmarket.domain.repository.ChatRoomRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void save(ChatMessageForm form) {
        ChatRoom chatRoom = this.findByRoomName(form.getRoomName())
            .orElseThrow(() -> new RuntimeException("존재하지 않는 채팅방이므로 메세지를 보낼 수 없습니다."));

        ChatMessage chatMessage = ChatMessage.builder()
            .userName(form.getSender())
            .message(form.getMessage())
            .chatRoom(chatRoom)
            .sendTime(LocalDateTime.now())
            .build();
        chatMessageRepository.save(chatMessage);
    }

    private Optional<ChatRoom> findByRoomName(String roomName) {
        return chatRoomRepository.findByRoomName(roomName);
    }
}
