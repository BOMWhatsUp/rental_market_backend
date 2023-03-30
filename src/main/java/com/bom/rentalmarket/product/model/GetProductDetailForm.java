package com.bom.rentalmarket.product.model;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.MaxPeriodType;
import com.bom.rentalmarket.product.type.StatusType;
import java.time.LocalDateTime;
import java.util.List;
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
public class GetProductDetailForm {

  private Long id;
  private String sellerId;
  private String sellerProfile;
  private String sellerRegion;
  private String nickname;
  private String title;
  private String content;
  private List<String> imageUrls;
  private String wishRegion;
  private MaxPeriodType maxRentalPeriod;
  private CategoryType categoryName;
  private Long unitPrice;
  private StatusType status;
  private LocalDateTime modifiedAt;
  private LocalDateTime returnDate;

  public static GetProductDetailForm from(ProductBoard productBoard, LocalDateTime returnDate,
      String sellerRegion, String sellerProfile) {
    return GetProductDetailForm.builder()
        .id(productBoard.getId())
        .sellerId(productBoard.getSellerId())
        .nickname(productBoard.getNickname())
        .title(productBoard.getTitle())
        .content(productBoard.getContent())
        .imageUrls(productBoard.getImageUrls())
        .wishRegion(productBoard.getWishRegion())
        .maxRentalPeriod(productBoard.getMaxRentalPeriod())
        .categoryName(productBoard.getCategoryName())
        .unitPrice(productBoard.getUnitPrice())
        .status(productBoard.getStatus())
        .modifiedAt(productBoard.getModifiedAt())
        .returnDate(returnDate)
        .sellerProfile(sellerProfile)
        .sellerRegion(sellerRegion)
        .build();
  }


}

