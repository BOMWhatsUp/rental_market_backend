package com.bom.rentalmarket.UserController.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


// 이메일 수정
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MemberNickNameUpdate {


    @NotBlank(message = "닉네임 입력은 필수 항목 입니다.")
    private String nickName;

    @NotBlank(message = "새 닉네임은 필수 항목 입니다.")
    @Size(min = 4, max = 20, message = "닉네임 입력은 4~20 사이의 길이로 입력해주세요. ")
    private String newNickName;

}
