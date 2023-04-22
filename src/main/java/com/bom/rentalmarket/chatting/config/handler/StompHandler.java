package com.bom.rentalmarket.chatting.config.handler;

import static com.bom.rentalmarket.chatting.exception.ErrorCode.REQUIRED_JOIN_MEMBER;
import static com.bom.rentalmarket.chatting.exception.ErrorCode.REQUIRED_LOGIN;

import com.bom.rentalmarket.UserController.domain.model.entity.Member;
import com.bom.rentalmarket.UserController.repository.MemberRepository;
import com.bom.rentalmarket.chatting.exception.ChatCustomException;
import com.bom.rentalmarket.jwt.JwtTokenProvider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println(accessor);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) { // websocket 연결요청
            System.out.println("웹소켓 연결 요청");
            String jwtToken = accessor.getFirstNativeHeader("Authorization");
            if(!jwtTokenProvider.validateToken(jwtToken)) {
                throw new ChatCustomException(REQUIRED_LOGIN);
            }
            String userId = jwtTokenProvider.getUserPk(jwtToken);
            if (userId != null) {
                log.debug("userId: {}", userId);
                Optional<Member> user = memberRepository.findByEmail(userId);
                if (user.isEmpty()) {
                    throw new ChatCustomException(REQUIRED_JOIN_MEMBER);
                }
            }
            System.out.println("완료");
        }
        return message;
    }
}
