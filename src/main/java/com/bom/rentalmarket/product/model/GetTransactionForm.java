package com.bom.rentalmarket.product.model;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.MaxPeriodType;
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
public class GetTransactionForm {

  private Long id;
  private String sellerId;
  private String nickname;
  private String title;
  private List<String> imageUrls;
  private MaxPeriodType maxRentalPeriod;
  private CategoryType categoryName;
  private Long unitPrice;

  public static GetTransactionForm from(ProductBoard productBoard) {
    return GetTransactionForm.builder()
        .id(productBoard.getId())
        .sellerId(productBoard.getSellerId().getEmail())
        .nickname(productBoard.getNickname())
        .title(productBoard.getTitle())
        .imageUrls(productBoard.getImageUrls())
        .maxRentalPeriod(productBoard.getMaxRentalPeriod())
        .categoryName(productBoard.getCategoryName())
        .unitPrice(productBoard.getUnitPrice())
        .build();
  }
}
