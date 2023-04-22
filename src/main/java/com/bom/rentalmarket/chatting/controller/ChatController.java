package com.bom.rentalmarket.chatting.controller;

import com.bom.rentalmarket.UserController.repository.MemberRepository;
import com.bom.rentalmarket.chatting.domain.chat.ChatMessageForm;
import com.bom.rentalmarket.chatting.exception.ChatCustomException;
import com.bom.rentalmarket.chatting.exception.ErrorCode;
import com.bom.rentalmarket.chatting.service.ChatRoomService;
import com.bom.rentalmarket.chatting.service.ChatService;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;
    private final KafkaTemplate<String, ChatMessageForm> kafkaTemplate;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageForm form) {
        if(!chatRoomService.checkUsers(form.getReceiver(), form.getSender())) {
            throw new ChatCustomException(ErrorCode.NONE_EXISTENT_MEMBER);
        }
        kafkaTemplate.send("chat", form);
    }

    @KafkaListener(topics = "chat")
    public void listenChatGroup(ConsumerRecord<String, ChatMessageForm> consumerRecord) {
        ChatMessageForm form = consumerRecord.value();
        chatService.saveMessage(form);
        messagingTemplate.convertAndSend("/sub/" + form.getRoomId(), form);
    }
}