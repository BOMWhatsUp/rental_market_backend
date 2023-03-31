package com.bom.rentalmarket.product.repository;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.entity.RentalHistory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {

  Optional<RentalHistory> findByProductIdAndSellerIdIsNotNullAndReturnYnFalse(ProductBoard productBoard);

  Page<RentalHistory> findByUserId(String userId, Pageable pageable);
  Page<RentalHistory> findBySellerId(String sellerId, Pageable pageable);
}
