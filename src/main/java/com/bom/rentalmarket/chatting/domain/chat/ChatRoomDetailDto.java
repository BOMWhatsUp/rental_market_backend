package com.bom.rentalmarket.chatting.domain.chat;

import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDetailDto {
    private String senderName;
    private String receiverName;

    private List<ChatMessage> messages;
}