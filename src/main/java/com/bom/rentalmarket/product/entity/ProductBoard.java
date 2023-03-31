package com.bom.rentalmarket.product.entity;

import com.bom.rentalmarket.UserController.domain.model.entity.Member;
import com.bom.rentalmarket.common.BaseEntity;
import com.bom.rentalmarket.product.converter.StringListConverter;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.MaxPeriodType;
import com.bom.rentalmarket.product.type.StatusType;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.envers.AuditOverride;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class ProductBoard extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  private CategoryType categoryName;

  @Enumerated(EnumType.STRING)
  private MaxPeriodType maxRentalPeriod;

  @Enumerated(EnumType.STRING)
  private StatusType status;

  @Convert(converter = StringListConverter.class)
  private List<String> imageUrls;

  private String mainImageUrl;

  private Long unitPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", referencedColumnName = "email")
  private Member sellerId;

  private String title;

  private String content;

  private String wishRegion;

  private String nickname;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;
}
