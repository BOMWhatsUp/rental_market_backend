package com.bom.rentalmarket.UserController.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MemberInput {

    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @NotBlank(message = "이메일은 필수 항목 입니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수 항목 입니다.")
    @Size(max = 20, message = "최대 20자를 넘길 수 없습니다.")
    private String nickName;

    @NotBlank(message = "비밀번호는 필수 항목 입니다.")
    @Size(min = 4, message = "비밀번호는 4자 이상입니다.")
    private String password;

    @NotBlank(message = "지역 입력은 필수 사항 입니다.")
    private String regin;

    private String filename;


}
