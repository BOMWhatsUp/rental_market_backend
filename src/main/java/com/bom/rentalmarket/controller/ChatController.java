package com.bom.rentalmarket.controller;

import com.bom.rentalmarket.domain.chat.ChatMessageForm;
import com.bom.rentalmarket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/message"
    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageForm form) {
        String receiver = form.getReceiver();
        System.out.println(receiver);
        chatService.save(form);
        simpMessagingTemplate.convertAndSend("/sub" + form.getRoomName(), form);
    }
}
