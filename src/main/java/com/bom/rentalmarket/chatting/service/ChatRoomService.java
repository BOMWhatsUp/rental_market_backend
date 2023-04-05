package com.bom.rentalmarket.chatting.service;

import static com.bom.rentalmarket.chatting.exception.ErrorCode.NONE_EXISTENT_MEMBER;
import static com.bom.rentalmarket.chatting.exception.ErrorCode.NOT_FOUND_CHATROOM;

import com.bom.rentalmarket.UserController.repository.MemberRepository;
import com.bom.rentalmarket.chatting.domain.chat.ChatListDto;
import com.bom.rentalmarket.chatting.domain.chat.ChatMessageForm;
import com.bom.rentalmarket.chatting.domain.chat.ChatRoomDetailDto;
import com.bom.rentalmarket.chatting.domain.chat.ReturnProductForm;
import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import com.bom.rentalmarket.chatting.domain.model.ChatRoom;
import com.bom.rentalmarket.chatting.domain.model.RegisterRoom;
import com.bom.rentalmarket.chatting.domain.repository.ChatMessageRepository;
import com.bom.rentalmarket.chatting.domain.repository.ChatRoomRepository;
import com.bom.rentalmarket.chatting.domain.repository.RegisterRoomRepository;
import com.bom.rentalmarket.chatting.exception.ChatCustomException;
import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.entity.RentalHistory;
import com.bom.rentalmarket.product.exception.ProductControllerAdvice.NotFoundException;
import com.bom.rentalmarket.product.repository.RentalHistoryRepository;
import com.bom.rentalmarket.product.type.StatusType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    private final MemberRepository memberRepository;
    private final RentalHistoryRepository rentalHistoryRepository;
    private final ChatService chatService;

    public List<ChatListDto> findAllRoom(String nickname) {
        List<ChatListDto> chatList = new ArrayList<>();
        List<RegisterRoom> registerRooms = registerRoomRepository.findAllByNickname(nickname);

        for (RegisterRoom room : registerRooms) {
            ChatRoom chatRoom = room.getChatRoom();
            String anotherUserNickname = this.findAnotherUser(chatRoom.getId(), nickname);
            Optional<ChatMessage> chatMessage = chatMessageRepository.findFirstByChatRoom_IdOrderBySendTimeDesc(
                chatRoom.getId());

            chatMessage.ifPresent(message -> chatList.add(ChatListDto.builder()
                .roomId(chatRoom.getId())
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
            throw new ChatCustomException(NOT_FOUND_CHATROOM);
        }

        ChatRoom chatRoom = optionalChatRoom.get();

        List<ChatMessage> messages = chatRoom.getMessages();

        List<RegisterRoom> registerRooms = registerRoomRepository.findByChatRoom_Id(
            chatRoom.getId());
        String receiverUserNickname = "";

        for (RegisterRoom room : registerRooms) {
            if (!room.getNickname().equals(senderNickname)) {
                messages.stream()
                    .filter(message -> message.getNickname().equals(room.getNickname()))
                    .forEach(m -> m.setRead(true));
            }
        }

        chatMessageRepository.saveAll(messages);

        return ChatRoomDetailDto.builder()
            .messages(messages)
            .senderName(senderNickname)
            .receiverName(receiverUserNickname)
            .product(chatRoom.getProduct())
            .build();
    }

    public Long connectRoomBetweenUsers(String receiver, String sender, ProductBoard product) {
        Long chatRoomId = checkAlreadyRoom(receiver, sender, product.getId());
        boolean checkUsers = this.checkUsers(receiver, sender);
        if(!checkUsers) {
            throw new ChatCustomException(NONE_EXISTENT_MEMBER);
        }

        if(chatRoomId != 0L) {
            return chatRoomId;
        }

        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setProduct(product);
        createRoom(receiver, chatRoom);
        createRoom(sender, chatRoom);

        return chatRoom.getId();
    }

    private void createRoom(String userNickname, ChatRoom chatRoom) {
        RegisterRoom registerRoom = RegisterRoom.register(userNickname, chatRoom);
        registerRoomRepository.save(registerRoom);
    }

    public void deleteRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new ChatCustomException(NOT_FOUND_CHATROOM));
        List<RegisterRoom> registerRooms = registerRoomRepository.findByChatRoom_Id(
            chatRoom.getId());
        if (registerRooms.isEmpty()) {
            throw new ChatCustomException(NOT_FOUND_CHATROOM);
        }

        registerRoomRepository.deleteAll(registerRooms);
    }

    public Long saveReturnProductMessage(ReturnProductForm form) {
        Long roomId = this.connectRoomBetweenUsers(form.getUserNickname(),
            form.getSellerNickname(), form.getProductId());

        String sendReturnNumberMessage = form.getUserNickname()
            + "님께서 물품 반납을 완료 하셨습니다. 운송장 번호 발송드립니다. \n"
            + "운송장 번호: " + form.getMessage();

        ChatMessageForm messageForm = ChatMessageForm.builder()
            .roomId(roomId)
            .receiver(form.getSellerNickname())
            .sender(form.getUserNickname())
            .message(sendReturnNumberMessage)
            .build();

        chatService.saveMessage(messageForm);

        RentalHistory rentalHistory = rentalHistoryRepository.findById(form.getId())
            .orElseThrow(() -> new NotFoundException("대여 기록을 찾을 수 없습니다."));

        rentalHistory.setStatusAndReturnYn(StatusType.RETURNED, true);

        ProductBoard productBoard = rentalHistory.getProductId();
        productBoard.setRentStatus(StatusType.AVAILABLE);

        rentalHistoryRepository.save(rentalHistory);

        return roomId;
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

    private Long checkAlreadyRoom(String receiver, String sender, Long productId) {
        List<RegisterRoom> receiverRooms = registerRoomRepository.findAllByNickname(receiver);
        Set<Long> receiverChatRooms = new HashSet<>();
        for(RegisterRoom registerRoom : receiverRooms){
            receiverChatRooms.add(registerRoom.getChatRoom().getId());
        }

        List<RegisterRoom> senderRooms = registerRoomRepository.findAllByNickname(sender);

        for(RegisterRoom senderChatroom : senderRooms) {
           if(receiverChatRooms.contains(senderChatroom.getChatRoom().getId())) {
               if(!Objects.equals(productId, senderChatroom.getChatRoom().getProduct().getId())) {
                   return 0L;
               }
               return senderChatroom.getChatRoom().getId();
           }
        }

        return 0L;
    }

    private boolean checkUsers(String receiver, String sender) {
        if(memberRepository.countByNickName(receiver) <= 0 || memberRepository.countByNickName(sender) <= 0) {
            return false;
        }

        return true;
    }
}
