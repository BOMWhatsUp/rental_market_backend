package com.bom.rentalmarket.product.model;

import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.MaxPeriodType;
import com.bom.rentalmarket.product.type.StatusType;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class CreateProductForm {

  @NotBlank
  private String sellerId;

  @NotBlank
  private String nickname;

  @NotBlank
  private String title;

  @NotBlank
  private String content;

  private List<String> imageUrls;

  private String mainImageUrl;

  @NotBlank
  private String wishRegion;

  @NotNull
  private MaxPeriodType maxRentalPeriod;

  @NotNull
  private CategoryType categoryName;

  @NotNull
  private Long unitPrice;

  @NotNull
  private StatusType status;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;
}



