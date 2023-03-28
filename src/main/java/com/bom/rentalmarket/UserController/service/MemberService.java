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







}
