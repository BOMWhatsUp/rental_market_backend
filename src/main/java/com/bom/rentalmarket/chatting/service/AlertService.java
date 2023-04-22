package com.bom.rentalmarket.chatting.service;

import com.bom.rentalmarket.chatting.domain.model.ChatMessage;
import com.bom.rentalmarket.chatting.domain.model.ChatRoom;
import com.bom.rentalmarket.chatting.domain.model.RegisterRoom;
import com.bom.rentalmarket.chatting.domain.repository.ChatMessageRepository;
import com.bom.rentalmarket.chatting.domain.repository.RegisterRoomRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlertService {
    private final RegisterRoomRepository registerRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter connect(SseEmitter emitter) {
        this.emitters.add(emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitters.size());
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitters.remove(emitter);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }

    public void messageAlert(String nickname) {
        List<RegisterRoom> registerRooms = registerRoomRepository.findAllByNickname(nickname);

        for(RegisterRoom registerRoom : registerRooms) {
            ChatRoom chatRoom = registerRoom.getChatRoom();
            Optional<ChatMessage> chatMessageOptional = chatMessageRepository.
                findTop1ByChatRoomAndNicknameOrderById(chatRoom, nickname);

            if(chatMessageOptional.isEmpty()) {
                continue;
            }

            ChatMessage chatMessage = chatMessageOptional.get();

            if(!chatMessage.isRead()) {
                emitters.forEach(emitter -> {
                    try {
                       emitter.send(SseEmitter.event()
                           .name("message")
                           .data("읽지 않은 메세지가 있습니다."));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            }
        }

    }
}
