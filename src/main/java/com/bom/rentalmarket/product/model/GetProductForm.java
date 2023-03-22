package com.bom.rentalmarket.product.model;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.MaxPeriodType;
import com.bom.rentalmarket.product.type.StatusType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProductForm {

  private Long id;
  private String sellerId;
  private String nickname;
  private String title;
  private String content;
  private String mainImageUrl;
  private String wishRegion;
  private MaxPeriodType maxRentalPeriod;
  private CategoryType categoryName;
  private Long unitPrice;
  private StatusType status;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static GetProductForm from(ProductBoard productBoard) {
    return GetProductForm.builder()
        .id(productBoard.getId())
        .sellerId(productBoard.getSellerId())
        .nickname(productBoard.getNickname())
        .title(productBoard.getTitle())
        .content(productBoard.getContent())
        .mainImageUrl(productBoard.getMainImageUrl())
        .wishRegion(productBoard.getWishRegion())
        .maxRentalPeriod(productBoard.getMaxRentalPeriod())
        .categoryName(productBoard.getCategoryName())
        .unitPrice(productBoard.getUnitPrice())
        .status(productBoard.getStatus())
        .createdAt(productBoard.getCreatedAt())
        .modifiedAt(productBoard.getModifiedAt())
        .build();
  }


}

