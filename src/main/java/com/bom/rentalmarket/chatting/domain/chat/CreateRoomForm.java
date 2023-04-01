package com.bom.rentalmarket.chatting.domain.chat;

import com.bom.rentalmarket.product.entity.ProductBoard;
import lombok.Getter;

@Getter
public class CreateRoomForm {
    private String receiverNickname;
    private String senderNickname;
    private ProductBoard product;
}