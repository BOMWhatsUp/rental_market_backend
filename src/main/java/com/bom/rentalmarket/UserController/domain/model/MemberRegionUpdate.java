package com.bom.rentalmarket.UserController.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MemberRegionUpdate {

    @NotBlank(message = "새 지역 입력은 필수 사항 입니다.")
    private String NewRegion;
}
