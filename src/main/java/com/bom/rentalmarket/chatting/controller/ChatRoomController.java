package com.bom.rentalmarket.chatting.controller;

import com.bom.rentalmarket.chatting.domain.chat.ChatListDto;
import com.bom.rentalmarket.chatting.domain.chat.ChatRoomDto;
import com.bom.rentalmarket.chatting.domain.chat.ChatRoomUsers;
import com.bom.rentalmarket.chatting.service.ChatRoomService;
import com.bom.rentalmarket.chatting.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/chat")
@RequiredArgsConstructor
@Controller
public class ChatRoomController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/room")
    public ResponseEntity<ChatRoomDto> getRoom(@RequestParam(value = "roomName") String roomName) {
        String userName = "seller";
        ChatRoomDto chatRoom = chatRoomService.findByRoomName(roomName, userName);

        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ChatListDto>> findAllRoom() {
        String userName = "seller";
        List<ChatListDto> chatList= chatRoomService.findAllRoom(userName);

        return ResponseEntity.ok(chatList);
    }

    @PostMapping("/room")
    public ResponseEntity<Void> createRoom(@RequestBody ChatRoomUsers user) {
        String roomName = chatRoomService.connectRoomBetweenUsers(user.getReceiverId(), user.getSenderId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/room/{roomName}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomName) {
        chatRoomService.deleteRoom(roomName);

        return ResponseEntity.ok().build();
    }


}