package com.bom.rentalmarket.chatting.controller;

import com.bom.rentalmarket.chatting.domain.chat.ChatListDto;
import com.bom.rentalmarket.chatting.domain.chat.ChatRoomDetailDto;
import com.bom.rentalmarket.chatting.domain.chat.ChatRoomUsers;
import com.bom.rentalmarket.chatting.service.ChatRoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/chat")
@RequiredArgsConstructor
@RestController
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/room")
    public ResponseEntity<ChatRoomDetailDto> getRoomDetail(@RequestParam(value = "roomId") Long roomId) {
        String userName = "seller";
        ChatRoomDetailDto chatRoomDetail = chatRoomService.roomDetail(roomId, userName);

        return ResponseEntity.ok(chatRoomDetail);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ChatListDto>> getAllChatRoom() {
        String userName = "seller";
        List<ChatListDto> chatList= chatRoomService.findAllRoom(userName);

        return ResponseEntity.ok(chatList);
    }

    @PostMapping("/room")
    public ResponseEntity<Void> createRoom(@RequestBody ChatRoomUsers user) {
        chatRoomService.connectRoomBetweenUsers(user.getReceiverId(), user.getSenderId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        chatRoomService.deleteRoom(roomId);

        return ResponseEntity.ok().build();
    }


}