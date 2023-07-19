package com.bom.rentalmarket.UserController.service;

import com.bom.rentalmarket.UserController.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginCheckService {

    private final MemberRepository memberRepository;

    public boolean checkEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean checkNickName(String nickName) {
        return memberRepository.existsByNickName(nickName);
    }
}
