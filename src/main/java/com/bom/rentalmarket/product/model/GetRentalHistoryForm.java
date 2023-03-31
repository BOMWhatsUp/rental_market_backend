package com.bom.rentalmarket.product.model;

import com.bom.rentalmarket.product.entity.RentalHistory;
import com.bom.rentalmarket.product.type.CategoryType;
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
public class GetRentalHistoryForm {

  private Long id;
  private Long productId;
  private String content;
  private String title;
  private StatusType status;
  private String mainImageUrl;
  private CategoryType categoryName;
  private String sellerProfile;
  private String sellerNickName;
  private Long totalPrice;
  private LocalDateTime rentalDate;
  private LocalDateTime returnDate;
  private Boolean returnYn;

  public static GetRentalHistoryForm from(RentalHistory rentalHistory){

    return GetRentalHistoryForm.builder()
        .id(rentalHistory.getId())
        .productId(rentalHistory.getProductId().getId())
        .content(rentalHistory.getContent())
        .title(rentalHistory.getTitle())
        .status(rentalHistory.getStatus())
        .mainImageUrl(rentalHistory.getMainImageUrl())
        .categoryName(rentalHistory.getCategoryName())
        .sellerProfile(rentalHistory.getSellerProfile())
        .sellerNickName(rentalHistory.getSellerNickName())
        .totalPrice(rentalHistory.getTotalPrice())
        .rentalDate(rentalHistory.getRentalDate())
        .returnDate(rentalHistory.getReturnDate())
        .returnYn(rentalHistory.getReturnYn())
        .build();
  }

}