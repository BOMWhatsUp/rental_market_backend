package com.bom.rentalmarket.chatting.service;

import com.bom.rentalmarket.chatting.domain.chat.ChatListDto;
import com.bom.rentalmarket.chatting.domain.chat.ChatRoomDto;
import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import com.bom.rentalmarket.chatting.domain.model.ChatRoom;
import com.bom.rentalmarket.chatting.domain.model.RegisterRoom;
import com.bom.rentalmarket.chatting.domain.repository.ChatMessageRepository;
import com.bom.rentalmarket.chatting.domain.repository.ChatRoomRepository;
import com.bom.rentalmarket.chatting.domain.repository.RegisterRoomRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final RegisterRoomRepository registerRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatListDto> findAllRoom(String userName) {
        List<ChatListDto> chatList = new ArrayList<>();
        List<RegisterRoom> registerRooms = registerRoomRepository.findAllByUserName(userName);

        for(RegisterRoom room : registerRooms) {
            ChatRoom chatRoom = room.getChatRoom();
            String anotherUserName = this.findAnotherUser(chatRoom.getId(), userName);
            ChatMessage chatMessage = chatMessageRepository.findFirstByChatRoom_IdOrderBySendTimeDesc(chatRoom.getId());

            if(chatMessage == null) {
                continue;
            }
            chatList.add(ChatListDto.builder()
                .receiverNickName(anotherUserName)
                .message(chatMessage.getMessage())
                .latelySenderDate(chatMessage.getSendTime())
                .build());
        }

        Collections.sort(chatList, new Comparator<ChatListDto>() {
            @Override
            public int compare(ChatListDto o1, ChatListDto o2) {
                if(o1.getLatelySenderDate().isAfter(o2.getLatelySenderDate())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        return chatList;
    }

    public ChatRoomDto findByRoomName(String roomName, String userName) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByRoomName(roomName);
        if(optionalChatRoom.isEmpty()) {
            throw new RuntimeException("해당 대화방이 존재하지 않습니다.");
        }
        ChatRoom chatRoom = optionalChatRoom.get();

        Optional<RegisterRoom> optionalRegisterRoom = registerRoomRepository.findByUserNameAndChatRoom_Id(userName,
            chatRoom.getId());
        if(optionalRegisterRoom.isEmpty()) {
            throw new RuntimeException("해당 대화방이 존재하지 않습니다.");
        }
        RegisterRoom registerRoom = optionalRegisterRoom.get();

        return ChatRoomDto.builder()
            .roomName(chatRoom.getRoomName())
            .userName(registerRoom.getUserName())
            .build();
    }

    public String connectRoomBetweenUsers(String receiver, String sender) {
        ChatRoom chatRoom = new ChatRoom();
        String roomId = UUID.randomUUID().toString();
        chatRoom.setRoomName(roomId);

        createRoom(receiver, chatRoom);
        chatRoomRepository.save(chatRoom);

        createRoom(sender, chatRoom);
        chatRoomRepository.save(chatRoom);

        return chatRoom.getRoomName();
    }

    private void createRoom(String userName, ChatRoom chatRoom) {
        RegisterRoom registerRoom = RegisterRoom.of(userName, chatRoom);
        registerRoomRepository.save(registerRoom);
    }

    @Transactional
    public void deleteRoom(String roomName) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(roomName)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 방입니다."));
        List<RegisterRoom> registerRooms = registerRoomRepository.findByChatRoom_Id(
            chatRoom.getId());
        if (registerRooms.isEmpty()) {
            throw new RuntimeException("존재하지 않는 방입니다.");
        }

        registerRoomRepository.deleteAll(registerRooms);
    }

    private String findAnotherUser(Long roomId, String myId) {
        List<RegisterRoom> registerRooms = registerRoomRepository.findByChatRoom_Id(roomId);
        for(RegisterRoom room : registerRooms) {
            if(!myId.equals(room.getUserName())) {
                return room.getUserName();
            }
        }
        return myId;
    }
}
