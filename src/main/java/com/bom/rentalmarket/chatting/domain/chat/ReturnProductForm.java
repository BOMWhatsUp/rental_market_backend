package com.bom.rentalmarket.chatting.domain.chat;

import com.bom.rentalmarket.product.entity.ProductBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnProductForm {

    private Long id;

    private ProductBoard productId;

    private String message;

    private String userNickname;

    private String sellerNickname;

    private Boolean returnYn;
}
