package com.bom.rentalmarket.chatting.controller;

import com.bom.rentalmarket.chatting.domain.chat.ChatListDto;
import com.bom.rentalmarket.chatting.domain.chat.ChatRoomDetailDto;
import com.bom.rentalmarket.chatting.domain.chat.CreateRoomForm;
import com.bom.rentalmarket.chatting.domain.chat.ReturnProductForm;
import com.bom.rentalmarket.chatting.service.ChatRoomService;
import com.bom.rentalmarket.jwt.JwtTokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/chat")
@RequiredArgsConstructor
@RestController
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final JwtTokenProvider provider;

    @GetMapping("/room")
    public ResponseEntity<ChatRoomDetailDto> getRoomDetail(
        @RequestParam(value = "roomId") String roomId,
        @RequestParam(value = "nickname") String senderNickname) {
        ChatRoomDetailDto chatRoomDetail = chatRoomService.roomDetail(Long.parseLong(roomId), senderNickname);

        return ResponseEntity.ok(chatRoomDetail);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ChatListDto>> getAllChatRoom(@RequestParam(value = "nickname") String nickname) {
        List<ChatListDto> chatList = chatRoomService.findAllRoom(nickname);

        return ResponseEntity.ok(chatList);
    }

    @PostMapping("/room")
    public ResponseEntity<String> createRoom(@RequestBody CreateRoomForm user) {
        String roomId = String.valueOf(chatRoomService.connectRoomBetweenUsers(
            user.getReceiverNickname(), user.getSenderNickname(), user.getProduct()));

        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnProduct(@RequestBody ReturnProductForm form) {
        String roomId = String.valueOf(chatRoomService.saveReturnProductMessage(form));

        return ResponseEntity.ok(roomId);
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(
        @PathVariable Long roomId,
        @RequestHeader(name="Authorization") String token) {
        chatRoomService.deleteRoom(roomId, provider.getUserPk(token));

        return ResponseEntity.ok().build();
    }
}