package com.bom.rentalmarket.chatting.service;

import com.bom.rentalmarket.UserController.repository.MemberRepository;
import com.bom.rentalmarket.chatting.domain.chat.ChatListDto;
import com.bom.rentalmarket.chatting.domain.chat.ChatRoomDetailDto;
import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import com.bom.rentalmarket.chatting.domain.model.ChatRoom;
import com.bom.rentalmarket.chatting.domain.model.RegisterRoom;
import com.bom.rentalmarket.chatting.domain.repository.ChatMessageRepository;
import com.bom.rentalmarket.chatting.domain.repository.ChatRoomRepository;
import com.bom.rentalmarket.chatting.domain.repository.RegisterRoomRepository;
import com.bom.rentalmarket.product.entity.ProductBoard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final RegisterRoomRepository registerRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatListDto> findAllRoom(String nickname) {
        List<ChatListDto> chatList = new ArrayList<>();
        List<RegisterRoom> registerRooms = registerRoomRepository.findAllByNickname(nickname);

        for (RegisterRoom room : registerRooms) {
            ChatRoom chatRoom = room.getChatRoom();
            String anotherUserNickname = this.findAnotherUser(chatRoom.getId(), nickname);
            Optional<ChatMessage> chatMessage = chatMessageRepository.findFirstByChatRoom_IdOrderBySendTimeDesc(
                chatRoom.getId());

            chatMessage.ifPresent(message -> chatList.add(ChatListDto.builder()
                .receiverNickName(anotherUserNickname)
                .message(message.getMessage())
                .latelySenderDate(message.getSendTime())
                .product(chatRoom.getProduct())
                .build()));
        }

        chatList.sort((o1, o2) -> {
            if (o1.getLatelySenderDate().isAfter(o2.getLatelySenderDate())) {
                return -1;
            } else {
                return 1;
            }
        });

        return chatList;
    }

    public ChatRoomDetailDto roomDetail(Long roomId, String senderNickname) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(roomId);
        if(optionalChatRoom.isEmpty()) {
            throw new RuntimeException("존재하지 않는 방입니다.");
        }

        ChatRoom chatRoom = optionalChatRoom.get();

        List<ChatMessage> messages = chatRoom.getMessages();
        messages.sort((m1, m2) -> m2.getId().compareTo(m1.getId()));

        List<RegisterRoom> registerRooms = registerRoomRepository.findByChatRoom_Id(
            chatRoom.getId());
        String receiverUserNickname = "";

        for (RegisterRoom room : registerRooms) {
            if (!room.getNickname().equals(senderNickname)) {
                receiverUserNickname = room.getNickname();
            }
        }

        return ChatRoomDetailDto.builder()
            .messages(messages)
            .senderName(senderNickname)
            .receiverName(receiverUserNickname)
            .product(chatRoom.getProduct())
            .build();
    }

    public Long connectRoomBetweenUsers(String receiver, String sender, ProductBoard product) {
        Long chatRoomId = checkAlreadyRoom(receiver, sender);
        if(chatRoomId != 0L) {
            return chatRoomId;
        }

        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setProduct(product);
        createRoom(receiver, chatRoom);
        createRoom(sender, chatRoom);

        return chatRoom.getId();
    }

    private void createRoom(String userName, ChatRoom chatRoom) {
        RegisterRoom registerRoom = RegisterRoom.register(userName, chatRoom);
        registerRoomRepository.save(registerRoom);
    }

    public void deleteRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new RuntimeException("이미 삭제된 방입니다."));
        List<RegisterRoom> registerRooms = registerRoomRepository.findByChatRoom_Id(
            chatRoom.getId());
        if (registerRooms.isEmpty()) {
            throw new RuntimeException("존재하지 않는 방입니다.");
        }

        registerRoomRepository.deleteAll(registerRooms);
    }

    private String findAnotherUser(Long roomId, String myNickname) {
        List<RegisterRoom> registerRooms = registerRoomRepository.findByChatRoom_Id(roomId);
        for (RegisterRoom room : registerRooms) {
            if (!myNickname.equals(room.getNickname())) {
                return room.getNickname();
            }
        }
        return myNickname;
    }

    private Long checkAlreadyRoom(String receiver, String sender) {
        List<RegisterRoom> receiverRooms = registerRoomRepository.findAllByNickname(receiver);
        Set<Long> receiverChatRooms = new HashSet<>();
        for(RegisterRoom registerRoom : receiverRooms){
            receiverChatRooms.add(registerRoom.getChatRoom().getId());
        }

        List<RegisterRoom> senderRooms = registerRoomRepository.findAllByNickname(sender);

        for(RegisterRoom registerRoom : senderRooms) {
           if(receiverChatRooms.contains(registerRoom.getChatRoom().getId())) {
               return registerRoom.getChatRoom().getId();
           }
        }

        return 0L;
    }
}
