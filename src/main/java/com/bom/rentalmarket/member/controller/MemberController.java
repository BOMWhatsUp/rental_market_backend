package com.bom.rentalmarket.member.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.bom.rentalmarket.member.MemberRepository;
import com.bom.rentalmarket.member.domain.exception.ExistsEmailException;
import com.bom.rentalmarket.member.domain.exception.PasswordNotMatchException;
import com.bom.rentalmarket.member.domain.exception.UserNotFoundException;
import com.bom.rentalmarket.member.domain.model.*;
import com.bom.rentalmarket.member.domain.model.entity.Member;
import com.bom.rentalmarket.member.domain.util.JWTUtils;
import com.bom.rentalmarket.member.domain.util.PasswordUtils;
import com.bom.rentalmarket.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/users/signup")
    public ResponseEntity<?> addUser(@RequestBody @Valid MemberInput memberInput, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }
        Member member = memberService.getAddUser(memberInput);

        memberRepository.save(member);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ExistsEmailException.class)
    public ResponseEntity<?> ExistsEmailExceptionHandler(ExistsEmailException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

     /*
     회원 정보 수정 하는 로직
     지역, 닉네임 정도만 수정 가능..?
     닉네임은 중복 불가까지..
     */
    @PutMapping("/users/modify/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id
            , @RequestBody @Valid MemberUpdate memberUpdate
            , Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.getUpdateUser(id, memberUpdate);
        memberRepository.save(member);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class, PasswordNotMatchException.class})
    public ResponseEntity<?> UserNotFoundExceptionHandler(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 회원 비밀번호 초기화
    @GetMapping("/user/password/reset/{id}")
    public ResponseEntity<?> resetUserPassword(@PathVariable Long id) {

        Member member = memberService.getResetUserPassword(id);
        memberRepository.save(member);

        return ResponseEntity.ok().build();
    }
// 회원 비밀번호 수정
//  MyPage 사용자 비밀번호 일치할때 비밀번호 수정 하는 API=======================================
    @PatchMapping("/user/{id}/reSetPassword/")
    public ResponseEntity<?> updateMemberPassword(@PathVariable Long id
            , @RequestBody MemberInputPassword memberInputPassword
            , Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }
        Member updateMember = memberService.getUpdateMemberPassword(id, memberInputPassword);

        memberRepository.save(updateMember);
        return ResponseEntity.ok().build();
    }



    // 회원 탈퇴하는 로직 단, 회원이 게시물을 올렸을 땐 회원 삭제가 안된다.
//    @DeleteMapping("/users/delete")
//    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
//
//        Member member = memberRepository.findById(id)
//                .orElseThrow(() -> new UsernameNotFoundException("사용자 정보가 없습니다."));
//        try {
//            memberRepository.delete(member);
//        } catch (DataIntegrityViolationException e) {
//            String message = "제약 조건 문제 발생";
//            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            String message = "회원탈퇴중 문제가 발생하였습니다.";
//            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//        }
//        return ResponseEntity.ok().build();
//    }

    // email, password 를 입력하여 -> JWT 토큰 발행 -> 1달간 유효성있는 JWT Token 발행
    @PostMapping("/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid MemberLogin memberLogin, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        Member member = memberRepository.findByEmail(memberLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다!"));

        if (!PasswordUtils.equalPassword(memberLogin.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.!");
        }


        // 토큰을 발행해 보자. -> builder 패턴을 할용하여 JWT 토큰 발행
        // 토큰은 key value 내려옴
        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(1);
        Date expireDate = java.sql.Timestamp.valueOf(localDateTime);

        String token = JWT.create()
                .withExpiresAt(expireDate)
                .withClaim("member_id", member.getId())
                .withSubject(member.getNickName())
                .withIssuer(member.getEmail())
                .sign(Algorithm.HMAC512("BOM".getBytes()));

        return ResponseEntity.ok().body(MemberLoginToken.builder().token(token).build());
    }


    // JWT 토큰을 재발행 하는 로직
    @PatchMapping("/users/refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("F-TOKEN");
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다."));

        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(1);

        Date expireDate = java.sql.Timestamp.valueOf(localDateTime);

        String newToken = JWT.create()
                .withExpiresAt(expireDate)
                .withClaim("member_id", member.getId())
                .withSubject(member.getNickName())
                .withIssuer(member.getEmail())
                .sign(Algorithm.HMAC512("BOM".getBytes()));

        return ResponseEntity.ok().body(MemberLoginToken.builder().token(newToken).build());
    }


    // JWT 토큰을 삭제하는 로직
//    @DeleteMapping("/user/login")
//    public ResponseEntity<?> removeToken(@RequestHeader("F-TOKEN") String token) {
//        String email = "";
//
//        try {
//            email = JWTUtils.getIssuer(token);
//        } catch (SignatureVerificationException e) {
//            return new ResponseEntity<>("토큰정보가 일치하지 않습니다", HttpStatus.BAD_REQUEST);
//        }
//
//        return ResponseEntity.ok().build();
//    }

}
