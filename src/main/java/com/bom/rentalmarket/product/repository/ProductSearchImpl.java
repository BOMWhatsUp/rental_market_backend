package com.bom.rentalmarket.product.repository;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.entity.QProductBoard;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.StatusType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ProductSearchImpl implements ProductSearch{

  private final JPAQueryFactory jpaQueryFactory;

  public ProductSearchImpl(JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  public List<ProductBoard> searchFilters(CategoryType categoryName, StatusType status,
      String keyword, Long pageNo, Long pageSize, String userRegion) {

    QProductBoard productBoard = QProductBoard.productBoard;

    BooleanBuilder builder = new BooleanBuilder();

    if (userRegion != null && !userRegion.isEmpty()) {
      builder.and(productBoard.wishRegion.containsIgnoreCase(userRegion));
    }

    if (categoryName != null) {
      builder.and(productBoard.categoryName.eq(categoryName));
    }

    if (status != null) {
      builder.and(productBoard.status.eq(status));
    }

    if (keyword != null && !keyword.isEmpty()) {
      builder.and(productBoard.title.containsIgnoreCase(keyword)
          .or(productBoard.content.containsIgnoreCase(keyword)));
    }

    return jpaQueryFactory.selectFrom(productBoard)
        .where(builder)
        .orderBy(productBoard.createdAt.desc())
        .offset((pageNo - 1) * pageSize) // 결과가 int의 범위를 벗어날수도 있기 때문
        .limit(pageSize)
        .fetch();
  }

}

