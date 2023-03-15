package com.bom.rentalmarket.UserController.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/*
회원id, 이름 name, 비밀번호 password, 지역 region, 닉네임 nickName

 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberLogin {

    // 로그인페이지 활용, 필요한 값
    @NotBlank(message = "이메일 항목은 필수 입력입니다.")
    private String email;

    @NotBlank(message = "비밀번호 항목은 필수 입력입니다.")
    private String password;

}
