package com.bom.rentalmarket.product.model;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.type.StatusType;
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
public class UpdateRentalHistoryForm {

  private Long id;
  private ProductBoard productId;
  private StatusType status;
  private boolean returnYn;

}
