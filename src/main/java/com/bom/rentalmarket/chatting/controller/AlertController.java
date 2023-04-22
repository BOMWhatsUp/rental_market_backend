package com.bom.rentalmarket.chatting.controller;

import com.bom.rentalmarket.chatting.domain.chat.NicknameVo;
import com.bom.rentalmarket.chatting.service.AlertService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
public class AlertController {
    private final AlertService alertService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        SseEmitter emitter = new SseEmitter(60 * 1000L);
        alertService.connect(emitter);
        try {
            emitter.send(SseEmitter.event()
                .name("connect")
                .data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(emitter);
    }

    @PostMapping("/message/alert")
    public ResponseEntity<Void> sendMessageAlert(@RequestBody NicknameVo nicknameVo) {
        alertService.messageAlert(nicknameVo.getNickname());

        return ResponseEntity.ok().build();
    }

}
