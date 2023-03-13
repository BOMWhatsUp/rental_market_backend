package com.bom.rentalmarket.member.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


// 이메일과 지역 수정
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MemberUpdate {

    @NotBlank(message = "닉네임은 필수 항목 입니다.")
    @Size(max = 20, message = "최대 20자를 넘길 수 없습니다.")
    private String nickName;

    @NotBlank(message = "지역 입력은 필수 사항 입니다.")
    private String regin;

}
