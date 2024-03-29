package com.bom.rentalmarket.chatting.domain.chat;

import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private String roomName;
    private String userName;
    private List<ChatMessage> messages;
}