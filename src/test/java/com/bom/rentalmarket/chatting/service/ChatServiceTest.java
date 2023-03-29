package com.bom.rentalmarket.chatting.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.bom.rentalmarket.chatting.domain.chat.ChatMessageForm;
import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import com.bom.rentalmarket.chatting.domain.model.ChatRoom;
import com.bom.rentalmarket.chatting.domain.repository.ChatMessageRepository;
import com.bom.rentalmarket.chatting.domain.repository.ChatRoomRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatService chatService;

    @Test
    @DisplayName("채팅 내용 저장 성공")
    void saveChatSuccess() {
        //given
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(1L);
        ChatMessage chatMessage = ChatMessage.builder()
            .id(chatRoom.getId())
            .chatRoom(chatRoom)
            .message("hello")
            .sendTime(LocalDateTime.now())
            .userName("sender")
            .build();

        given(chatRoomRepository.findById(chatRoom.getId()))
            .willReturn(Optional.of(chatRoom));
        given(chatMessageRepository.save(any()))
            .willReturn(chatMessage);

        ArgumentCaptor<ChatMessage> captor = ArgumentCaptor.forClass(ChatMessage.class);

        //when
        chatService.saveMessage(ChatMessageForm.builder()
                .message("hello")
                .sender("sender")
                .receiver("receiver")
                .roomId(1L)
                .build());

        //then
        verify(chatMessageRepository, times(1)).save(captor.capture());
        assertEquals("hello", captor.getValue().getMessage());
        assertEquals("sender", captor.getValue().getUserName());
        assertEquals(1L, captor.getValue().getChatRoom().getId());
    }
}