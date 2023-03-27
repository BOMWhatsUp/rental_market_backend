package com.bom.rentalmarket.product.repository;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.entity.RentalHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {

  Optional<RentalHistory> findByProductIdAndReturnYnFalse(ProductBoard productBoard);
}
