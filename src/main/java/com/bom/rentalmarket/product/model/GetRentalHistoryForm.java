package com.bom.rentalmarket.product.model;

import com.bom.rentalmarket.UserController.domain.model.entity.Member;
import com.bom.rentalmarket.product.entity.ProductBoard;
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
  private ProductBoard productId;
  private Member userId;
  private Member sellerId;
  private Long totalPrice;
  private LocalDateTime rentalDate;
  private LocalDateTime returnDate;
  private Boolean returnYn;

}
