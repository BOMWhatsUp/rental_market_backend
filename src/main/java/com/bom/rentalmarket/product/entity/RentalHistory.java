package com.bom.rentalmarket.product.entity;

import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.StatusType;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentalHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private ProductBoard productId;

  private String content;

  private String title;

  private StatusType status;

  private String mainImageUrl;

  private CategoryType categoryName;

  private String userId;

  private String sellerId;

  private String sellerProfile;

  private String sellerNickName;

  private Long totalPrice;

  private LocalDateTime rentalDate;

  private LocalDateTime returnDate;

  private Boolean returnYn;

}