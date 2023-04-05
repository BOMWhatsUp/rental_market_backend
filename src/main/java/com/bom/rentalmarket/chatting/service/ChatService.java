package com.bom.rentalmarket.chatting.service;

import static com.bom.rentalmarket.chatting.exception.ErrorCode.*;

import com.bom.rentalmarket.chatting.domain.chat.ChatMessageForm;
import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import com.bom.rentalmarket.chatting.domain.model.ChatRoom;
import com.bom.rentalmarket.chatting.domain.repository.ChatMessageRepository;
import com.bom.rentalmarket.chatting.domain.repository.ChatRoomRepository;
import com.bom.rentalmarket.chatting.exception.ChatCustomException;
import com.bom.rentalmarket.chatting.exception.ErrorCode;
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
            .orElseThrow(() -> new ChatCustomException(NOT_FOUND_CHATROOM));

        chatMessageRepository.save(ChatMessage.builder()
            .nickname(form.getSender())
            .message(form.getMessage())
            .chatRoom(chatRoom)
            .sendTime(LocalDateTime.now())
            .build());
    }
}