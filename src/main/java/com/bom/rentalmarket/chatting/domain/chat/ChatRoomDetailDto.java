package com.bom.rentalmarket.chatting.domain.chat;

import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDetailDto {
    private String senderName;
    private String senderEmail;
    private String receiver;
    private List<ChatMessage> messages;
    private String chatRoomName;
}
