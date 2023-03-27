package com.bom.rentalmarket.product.repository;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.StatusType;
import java.util.List;

public interface ProductSearch {
  List<ProductBoard> searchFilters(CategoryType categoryName, StatusType status,
      String keyword, long pageNo, long pageSize);
}
