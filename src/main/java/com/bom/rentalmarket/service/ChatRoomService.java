package com.bom.rentalmarket.service;

import com.bom.rentalmarket.domain.chat.ChatRoomDto;
import com.bom.rentalmarket.domain.model.ChatRoom;
import com.bom.rentalmarket.domain.model.RegisterRoom;
import com.bom.rentalmarket.domain.repository.ChatRoomRepository;
import com.bom.rentalmarket.domain.repository.RegisterRoomRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final RegisterRoomRepository registerRoomRepository;

    public List<ChatRoomDto> findAllRoom(String userName) {
        List<RegisterRoom> registerRooms = registerRoomRepository.findByUserName(userName);

        return registerRooms.stream().
            map(room -> ChatRoomDto.builder()
                .roomName(room.getChatRoom().getRoomName())
                .messages(room.getChatRoom().getMessages())
                .userName(room.getUserName())
                .build())
            .collect(Collectors.toList());
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

}
