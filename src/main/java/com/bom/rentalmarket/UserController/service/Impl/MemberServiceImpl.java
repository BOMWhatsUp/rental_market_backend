package com.bom.rentalmarket.UserController.service.Impl;

import com.bom.rentalmarket.UserController.domain.exception.ExistsEmailException;
import com.bom.rentalmarket.UserController.domain.exception.PasswordNotMatchException;
import com.bom.rentalmarket.UserController.domain.exception.UserNotFoundException;
import com.bom.rentalmarket.UserController.domain.model.MemberInput;
import com.bom.rentalmarket.UserController.domain.model.MemberInputPassword;
import com.bom.rentalmarket.UserController.domain.model.entity.Member;
import com.bom.rentalmarket.UserController.repository.MemberRepository;
import com.bom.rentalmarket.UserController.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;


    private String getEncryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    @Override //회원가입
    public Member getAddUser(MemberInput memberInput) {

        // 중복체크
        if (memberRepository.countByEmail(memberInput.getEmail()) > 0
                || memberRepository.countByNickName(memberInput.getNickName()) > 0) {
            throw new ExistsEmailException("이미 존재한 회원 입니다.");
        }

        String encPassword = getEncryptPassword(memberInput.getPassword());
        return Member.builder()
                .email(memberInput.getEmail())
                .nickName(memberInput.getNickName())
                .password(encPassword)
                .regin(memberInput.getRegin())
                .regDate(LocalDateTime.now())
                .build();
    }


    @ExceptionHandler(value = {UsernameNotFoundException.class, PasswordNotMatchException.class})
    public ResponseEntity<?> UserNotFoundExceptionHandler(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @Override
    public Member getResetUserPassword(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        // 비밀번호 초기화 -> DB 저장 -> 전송
        String resetPassword = getResetPassword();
        String resetEncryptPassword = getEncryptPassword(resetPassword);
        member.setPassword(resetEncryptPassword);
        memberRepository.save(member);

        String message = String.format("[%s]님의 임시 비밀번호가 [%s]로 초기화 되었습니다."
                , member.getNickName()
                , resetPassword);
        sendSMS(message);

        return Member.builder().build();
    }

    void sendSMS(String message) {
        System.out.println("[WEB 발신]");
        System.out.println(message);
    }

    private String getResetPassword() {
        return  UUID.randomUUID().toString().replaceAll("-","").substring(0,10);
    }


    @Override
    public Member getUpdateMemberPassword(Long id, MemberInputPassword memberInputPassword) {

        Member member = memberRepository.findByIdAndPassword(id, memberInputPassword.getPassword())
                .orElseThrow(() -> new PasswordNotMatchException("비밀번호가 일치하지 않습니다."));

        member.setPassword(memberInputPassword.getNewPassword());

        return Member.builder().build();
    }


}

