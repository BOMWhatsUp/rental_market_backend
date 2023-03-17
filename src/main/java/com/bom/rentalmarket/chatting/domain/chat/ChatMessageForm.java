package com.bom.rentalmarket.chatting.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageForm {

    private String roomName;
    private String sender;
    private String receiver;
    private String userProfile;
    private String message;

}