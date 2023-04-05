package com.bom.rentalmarket.chatting.domain.chat;

import com.bom.rentalmarket.product.entity.ProductBoard;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatListDto {
    private Long roomId;
    private String receiverNickName;
    private String receiverImageUrl;
    private LocalDateTime latelySenderDate;
    private String message;
    private ProductBoard product;

}