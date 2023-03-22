package com.bom.rentalmarket.chatting.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.bom.rentalmarket.chatting.domain.chat.ChatListDto;
import com.bom.rentalmarket.chatting.domain.chat.ChatRoomUsers;
import com.bom.rentalmarket.chatting.domain.model.ChatRoom;
import com.bom.rentalmarket.chatting.domain.model.RegisterRoom;
import com.bom.rentalmarket.chatting.domain.repository.ChatRoomRepository;
import com.bom.rentalmarket.chatting.service.ChatRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @MockBean
    private ChatRoomService chatRoomService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Test
    void successCreateChatRoom() throws Exception {
        //given
        String receiver = "receiver";
        String sender = "sender";
        willDoNothing().given(chatRoomService).connectRoomBetweenUsers(receiver, sender);

        //when
        //then
        mockMvc.perform(post("/chat/room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new ChatRoomUsers()
                )))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void successDeleteChatRoom() throws Exception {
        //given
        Long chatRoomId = 1L;
        willDoNothing().given(chatRoomService).deleteRoom(chatRoomId);

        //when
        //then
        mockMvc.perform(delete("/chat/room/{roomId}", chatRoomId))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void findAllRoom() throws Exception {
        //given
        List<ChatListDto> chatListDto = Arrays.asList(
            ChatListDto.builder()
                .message("hello")
                .latelySenderDate(LocalDateTime.now())
                .receiverNickName("client")
                .build(),
            ChatListDto.builder()
                .message("hello1")
                .latelySenderDate(LocalDateTime.now())
                .receiverNickName("client2")
                .build(),
            ChatListDto.builder()
                .message("hello2")
                .latelySenderDate(LocalDateTime.now())
                .receiverNickName("client3")
                .build());

        given(chatRoomService.findAllRoom(anyString()))
            .willReturn(chatListDto);

        //when
        //then
        mockMvc.perform(get("/chat/list"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].message").value("hello"))
            .andExpect(jsonPath("$[0].receiverNickName").value("client"))
            .andExpect(jsonPath("$[1].message").value("hello1"))
            .andExpect(jsonPath("$[1].receiverNickName").value("client2"))
            .andExpect(jsonPath("$[2].message").value("hello2"))
            .andExpect(jsonPath("$[2].receiverNickName").value("client3"));
    }
}