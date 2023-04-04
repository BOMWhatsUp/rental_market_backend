package com.bom.rentalmarket.UserController.controller;


import com.bom.rentalmarket.UserController.domain.exception.*;
import com.bom.rentalmarket.UserController.domain.model.*;
import com.bom.rentalmarket.UserController.domain.model.entity.Member;
import com.bom.rentalmarket.UserController.repository.MemberRepository;
import com.bom.rentalmarket.jwt.JwtTokenProvider;
import com.bom.rentalmarket.s3.S3Service;
import com.bom.rentalmarket.UserController.service.LoginCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    private final LoginCheckService loginCheckService;

    private final S3Service s3Service;


    // email 중복  error Message 보내주는 로직
    @ExceptionHandler(ExistsEmailException.class)
    public ResponseEntity<?> ExistsEmailExceptionHandler(ExistsEmailException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistsNickNameException.class)
    public ResponseEntity<?> ExistsNameExceptionHandler(ExistsNickNameException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> UserNotFoundExceptionHandler(UserNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    public ResponseEntity<?> NotMatchPasswordExceptionHandler(NotMatchPasswordException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 회원가입, 중복체크 나누기
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> signup(@RequestBody @Valid MemberInput memberInput) {

        Member member = Member.builder()
                .email(memberInput.getEmail())
                .nickName(memberInput.getNickName())
                .password(passwordEncoder.encode(memberInput.getPassword()))//비밀번호 인코딩
                .region(memberInput.getRegion())
                .regDate(LocalDateTime.now())
                .roles(Collections.singletonList("ROLE_USER")) //roles는 최초 USER로 설정
                .imageUrl(memberInput.getImageUrl())
                .build();

        memberRepository.save(member);
        return ResponseEntity.ok().body(memberInput);
    }

    // email 중복 검사
    @GetMapping("/check/email/{email}")
    public ResponseEntity<Boolean> checkByEmail(@PathVariable String email) {
        return ResponseEntity.ok(loginCheckService.checkEmail(email));
    }

    // nickName 증복 검사
    @GetMapping("/check/nickName/{nickName}")
    public ResponseEntity<Boolean> checkByNickName(@PathVariable String nickName) {
        return ResponseEntity.ok(loginCheckService.checkNickName(nickName));
    }

     /*
     회원 정보 수정 하는 로직
     지역, 닉네임 정도만 수정 가능..?
     닉네임은 중복 불가까지..
     */

    @PatchMapping("/update/NickName/{id}")
    public ResponseEntity<?> updateNickName(@PathVariable Long id
            , @RequestBody MemberNickNameUpdate memberNickNameUpdate
            , Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        Member member = memberRepository.findByIdAndNickName(id, memberNickNameUpdate.getNickName())
                .orElseThrow(() -> new ExistsNickNameException("닉네임이 일치하지 않습니다"));

        member.setNickName(memberNickNameUpdate.getNewNickName());
        member.setUpdateDate(LocalDateTime.now());
        memberRepository.save(member);

        return ResponseEntity.ok().body(memberNickNameUpdate);
    }


    @PatchMapping("/update/Region/{id}")
    public ResponseEntity<?> updateRegion(@PathVariable Long id
            , @RequestBody MemberRegionUpdate memberRegionUpdate
            , Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }
        Member member = memberRepository.findByIdAndRegion(id, memberRegionUpdate.getRegion())
                .orElseThrow(() -> new ExistsRegionException("지역 정보가 일치하지 않습니다."));

        member.setRegion(memberRegionUpdate.getNewRegion());
        member.setUpdateDate(LocalDateTime.now());

        memberRepository.save(member);
        return ResponseEntity.ok().body(memberRegionUpdate);
    }

    // 마이페이지 프로필 수정, 로직 다시 만들어야 함
    @PatchMapping("/upload/{id}")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        String fileName = s3Service.upload(multipartFile);
        return ResponseEntity.ok().body(fileName);
    }

    // 마이페이지 회원 비밀번호 수정
//  MyPage 사용자 비밀번호 일치할때 비밀번호 수정 하는 API=======================================
//    @PatchMapping("/user/{id}/reSetPassword/")
//    public ResponseEntity<?> updateMemberPassword(@PathVariable Long id
//            , @RequestBody MemberInputPassword memberInputPassword
//            , Errors errors) {
//
//        List<ResponseError> responseErrorList = new ArrayList<>();
//        if (errors.hasErrors()) {
//            errors.getAllErrors().forEach((e) -> {
//                responseErrorList.add(ResponseError.of((FieldError) e));
//            });
//            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
//        }
//        Member updateMember = memberRepository.getUpdateMemberPassword(id, memberInputPassword);
//
//        memberRepository.save(updateMember);
//        return ResponseEntity.ok().build();
//    }


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

    // email, password 를 입력하여 -> JWT 토큰 발행 -> 30분 유효성있는 JWT Token 발행
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid MemberDto memberDto
            , Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        Member member = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("가입되지 않은 E-MAIL 입니다."));

        if (!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new NotMatchPasswordException("잘못된 비밀번호입니다.");
        }

        String token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles()
                , member.getNickName(), member.getRegion(), member.getImageUrl());

        // 로그인에 성공하면 email, roles 로 토큰 생성 후 반환
        return ResponseEntity.ok().body(token);
    }

}
