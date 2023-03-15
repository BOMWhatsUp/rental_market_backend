package com.bom.rentalmarket.UserController.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInputPassword {

    @NotBlank(message = "현재 비밀번호는 필수 항목입니다.")
    @Size(min = 4, message = "비밀번호는 4자 이상입니다.")
    private String password; // 현재 비밀번호


    @NotBlank(message = "신규 비밀번호는 필수 항목입니다.")
    @Size(min = 4, max = 20, message = "비밀번호는 4 ~ 20 사이의 길이로 입력해주세요!")
    private String newPassword; // 새로운 비밀번호


}
