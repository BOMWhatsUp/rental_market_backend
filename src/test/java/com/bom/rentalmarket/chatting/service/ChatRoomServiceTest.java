package com.bom.rentalmarket.chatting.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.bom.rentalmarket.chatting.domain.chat.ChatListDto;
import com.bom.rentalmarket.chatting.domain.chat.ChatRoomDetailDto;
import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import com.bom.rentalmarket.chatting.domain.model.ChatRoom;
import com.bom.rentalmarket.chatting.domain.model.RegisterRoom;
import com.bom.rentalmarket.chatting.domain.repository.ChatMessageRepository;
import com.bom.rentalmarket.chatting.domain.repository.ChatRoomRepository;
import com.bom.rentalmarket.chatting.domain.repository.RegisterRoomRepository;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private RegisterRoomRepository registerRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Test
    @DisplayName("전체 채팅방 조회 성공")
    void findAllRoom()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //given
        ChatRoom chatRoom = new ChatRoom();

        List<RegisterRoom> registerRooms = Arrays.asList(
            RegisterRoom.builder()
                .userName("seller")
                .chatRoom(chatRoom)
                .build(),
            RegisterRoom.builder()
                .userName("client")
                .chatRoom(chatRoom)
                .build());
        given(registerRoomRepository.findAllByUserName(anyString()))
            .willReturn(registerRooms);
        given(chatMessageRepository.findFirstByChatRoom_IdOrderBySendTimeDesc(chatRoom.getId()))
            .willReturn(Optional.of(ChatMessage.builder()
                .chatRoom(chatRoom)
                .sendTime(LocalDateTime.now())
                .message("hello")
                .userName("seller")
                .build()));
        given(registerRoomRepository.findByChatRoom_Id(chatRoom.getId()))
            .willReturn(Arrays.asList(
                RegisterRoom.builder()
                    .chatRoom(chatRoom)
                    .userName("seller")
                    .build(),
                RegisterRoom.builder()
                    .chatRoom(chatRoom)
                    .userName("client")
                    .build()));

        //private method
        Method method = ChatRoomService.class.getDeclaredMethod("findAnotherUser", Long.class, String.class);
        method.setAccessible(true);

        String anotherUser = (String)method.invoke(chatRoomService, chatRoom.getId(), "seller");
        System.out.println(anotherUser);

        //when
        List<ChatListDto> chatListDtos = chatRoomService.findAllRoom("seller");

        //then
        verify(registerRoomRepository, times(1)).findAllByUserName(anyString());
        verify(chatMessageRepository, times(2)).findFirstByChatRoom_IdOrderBySendTimeDesc(
            chatRoom.getId());
        verify(registerRoomRepository, atLeastOnce()).findByChatRoom_Id(chatRoom.getId());
        assertEquals("hello", chatListDtos.get(0).getMessage());
        assertEquals("client", chatListDtos.get(0).getReceiverNickName());
        assertEquals("hello", chatListDtos.get(1).getMessage());
        assertEquals("client", chatListDtos.get(1).getReceiverNickName());
    }

    @Test
    @DisplayName("채팅방 상세 내용 출력 성공")
    void printRoomDetail()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(1L);

        List<ChatMessage> messages = Arrays.asList(
            ChatMessage.builder()
                .message("hello")
                .userName("seller")
                .sendTime(LocalDateTime.now())
                .chatRoom(chatRoom)
                .build(),
            ChatMessage.builder()
                .message("hello2")
                .userName("seller")
                .sendTime(LocalDateTime.now())
                .chatRoom(chatRoom)
                .build(),
            ChatMessage.builder()
                .message("hello3")
                .userName("seller")
                .sendTime(LocalDateTime.now())
                .chatRoom(chatRoom)
                .build());

        List<RegisterRoom> registerRooms = Arrays.asList(
            RegisterRoom.builder()
                .userName("seller")
                .chatRoom(chatRoom)
                .build(),
            RegisterRoom.builder()
                .userName("client")
                .chatRoom(chatRoom)
                .build());

        given(chatRoomRepository.findById(chatRoom.getId()))
            .willReturn(Optional.of(chatRoom));
        given(registerRoomRepository.findByChatRoom_Id(chatRoom.getId()))
            .willReturn(registerRooms);

        //when
        ChatRoomDetailDto chatRoomDetailDto = chatRoomService.roomDetail(chatRoom.getId(), "seller");

        //then
        assertEquals("seller", chatRoomDetailDto.getSenderName());
        assertEquals("client", chatRoomDetailDto.getReceiverName());
        assertEquals(1L, messages.get(0).getChatRoom().getId());
        assertEquals(1L, messages.get(1).getChatRoom().getId());
        assertEquals(1L, messages.get(2).getChatRoom().getId());
    }

    @Test
    @DisplayName("채팅방 생성 성공")
    void createChatRoom()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(1L);
        String receiver = "seller";
        String sender = "client";

        Method method = ChatRoomService.class.getDeclaredMethod("createRoom", String.class, ChatRoom.class);
        method.setAccessible(true);

        method.invoke(chatRoomService, receiver, chatRoom);
        method.invoke(chatRoomService, sender, chatRoom);

        ArgumentCaptor<RegisterRoom> captor = ArgumentCaptor.forClass(RegisterRoom.class);

        //when
        chatRoomService.connectRoomBetweenUsers(receiver, sender);

        //then
        verify(registerRoomRepository, times(4)).save(captor.capture());
        assertEquals(1L, captor.getAllValues().get(0).getChatRoom().getId());
        assertEquals("seller", captor.getAllValues().get(0).getUserName());
        assertEquals(1L, captor.getAllValues().get(1).getChatRoom().getId());
        assertEquals("client", captor.getAllValues().get(1).getUserName());
    }

    @Test
    @DisplayName("채팅방 삭제 성공")
    void deleteChatRoom() {
        //given
        ChatRoom chatRoom = new ChatRoom();

        List<RegisterRoom> registerRooms = Arrays.asList(
            RegisterRoom.builder()
                .userName("seller")
                .chatRoom(chatRoom)
                .build(),
            RegisterRoom.builder()
                .userName("client")
                .chatRoom(chatRoom)
                .build());
        given(chatRoomRepository.findById(chatRoom.getId()))
            .willReturn(Optional.of(chatRoom));
        given(registerRoomRepository.findByChatRoom_Id(chatRoom.getId()))
            .willReturn(registerRooms);

        //when
        chatRoomService.deleteRoom(chatRoom.getId());

        //then
        verify(registerRoomRepository, times(1)).deleteAll(registerRooms);
    }
}