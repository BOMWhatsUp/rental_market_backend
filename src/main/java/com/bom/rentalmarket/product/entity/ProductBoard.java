package com.bom.rentalmarket.product.entity;

import com.bom.rentalmarket.common.BaseEntity;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.MaxPeriodType;
import com.bom.rentalmarket.product.type.StatusType;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "ProductBoard")
public class ProductBoard extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private CategoryType categoryName;

  @Enumerated(EnumType.STRING)
  private MaxPeriodType maxRentalPeriod;

  @Enumerated(EnumType.STRING)
  private StatusType status;

  @ElementCollection
  private List<String> imageUrls;

  private String mainImageUrl;

  private Long unitPrice;

  private String sellerId;

  private String title;

  private String content;

  private String wishRegion;

  private String nickname;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;
}
