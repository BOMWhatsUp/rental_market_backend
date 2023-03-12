package com.bom.rentalmarket.controller;

import com.bom.rentalmarket.domain.chat.ChatRoomDto;
import com.bom.rentalmarket.domain.chat.ChatRoomUsers;
import com.bom.rentalmarket.service.ChatRoomService;
import com.bom.rentalmarket.service.ChatService;
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
    public ResponseEntity<List<ChatRoomDto>> getRooms() {
        String userName = "seller";
        List<ChatRoomDto> chatRoomDtos = chatRoomService.findAllRoom(userName);
        return ResponseEntity.ok(chatRoomDtos);
    }

    @PostMapping("/room")
    public ResponseEntity<String> createRoom(@RequestBody ChatRoomUsers user) {
        String roomId = chatRoomService.connectRoomBetweenUsers(user.getReceiverId(), user.getSenderId());

        return ResponseEntity.ok(roomId);
    }

    @DeleteMapping("/room/{roomName}")
    public ResponseEntity<String> deleteRoom(@PathVariable String roomName) {
        chatRoomService.deleteRoom(roomName);

        return ResponseEntity.ok("삭제완료");
    }


}
