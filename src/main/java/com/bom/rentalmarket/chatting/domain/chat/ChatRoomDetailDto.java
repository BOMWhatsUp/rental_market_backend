package com.bom.rentalmarket.chatting.domain.chat;

import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import com.bom.rentalmarket.product.entity.ProductBoard;
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
    private ProductBoard product;

    private List<ChatMessage> messages;
}