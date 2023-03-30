package com.bom.rentalmarket.chatting.domain.chat;

import com.bom.rentalmarket.product.entity.ProductBoard;
import lombok.Getter;

@Getter
public class ChatRoomUsers {
    private String receiverId;
    private String senderId;
    private ProductBoard product;
}