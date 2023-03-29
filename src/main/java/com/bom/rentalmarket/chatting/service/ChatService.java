package com.bom.rentalmarket.chatting.service;

import com.bom.rentalmarket.chatting.domain.chat.ChatMessageForm;
import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import com.bom.rentalmarket.chatting.domain.model.ChatRoom;
import com.bom.rentalmarket.chatting.domain.repository.ChatMessageRepository;
import com.bom.rentalmarket.chatting.domain.repository.ChatRoomRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void saveMessage(ChatMessageForm form) {
        ChatRoom chatRoom = chatRoomRepository.findById(form.getRoomId())
            .orElseThrow(() -> new RuntimeException("존재하지 않는 채팅방이므로 메세지를 보낼 수 없습니다."));

        chatMessageRepository.save(ChatMessage.builder()
            .userName(form.getSender())
            .message(form.getMessage())
            .chatRoom(chatRoom)
            .sendTime(LocalDateTime.now())
            .build());
    }
}