package com.bom.rentalmarket.product.model;

import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.MaxPeriodType;
import com.bom.rentalmarket.product.type.StatusType;
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
  private String title;

  @NotBlank
  private String contents;

  @NotBlank
  private List<String> imageUrls;

  @NotBlank
  private String region;

  @NotNull
  private MaxPeriodType maxPeriod;

  @NotNull
  private CategoryType categoryName;

  @NotNull
  private String unitPrice;

  @NotNull
  private StatusType status;

}
