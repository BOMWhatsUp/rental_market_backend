package com.bom.rentalmarket.chatting.domain.chat;

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
    private String receiverNickName;
    private String receiverImageUrl;
    private LocalDateTime latelySenderDate;
    private String message;
    private Long productId;

}