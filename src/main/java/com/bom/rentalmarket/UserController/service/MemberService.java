package com.bom.rentalmarket.UserController.service;


import com.bom.rentalmarket.UserController.domain.model.MemberInput;
import com.bom.rentalmarket.UserController.domain.model.MemberInputPassword;
import com.bom.rentalmarket.UserController.domain.model.entity.Member;

public interface MemberService {


   /*
      회원 가입
      email(중복확인)
      nickName(중복확인)
      password
      지역
       */
   Member getAddUser(MemberInput memberInput);

   /*
   회원 정보 수정
   지역, 닉네임은 수정할 수 있도록 한다
    */
//   Member getUpdateUser(Long id, MemberUpdate memberUpdate);

   /*
   임시 비밀번호를 만들어 비밀번호 변경
    */
   Member getResetUserPassword(Long id);

   /*
   임시비밀번호와 newPassword 를 작성하여 비밀번호 변경
    */
   Member getUpdateMemberPassword(Long id, MemberInputPassword memberInputPassword);

   /*
   회원탈퇴 -> 회원이 게시물을 올렸을 땐 회원 삭제가 안된다.
    */


   /*
    email, password 를 입력하여 -> JWT 토큰 발행 -> 1달간 유효성있는 JWT Token 발행
    */







}
