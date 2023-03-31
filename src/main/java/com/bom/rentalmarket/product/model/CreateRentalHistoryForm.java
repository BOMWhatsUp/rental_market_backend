package com.bom.rentalmarket.product.model;

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
public class CreateRentalHistoryForm {

  private Long id;
  private Long productId;
  private String userId;
  private String sellerId;
  private Long totalPrice;
  private LocalDateTime rentalDate;
  private LocalDateTime returnDate;
  private Boolean returnYn;
  private String message;
  private Long roomId;

}
