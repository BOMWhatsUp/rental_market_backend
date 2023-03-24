package com.bom.rentalmarket.product.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bom.rentalmarket.product.entity.ProductBoard;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
public class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @Test
  @Sql(scripts = {"classpath:schema.sql",
      "classpath:data.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  void testFindAll() {
    List<ProductBoard> productList = productRepository.findAll();
    assertEquals(7, productList.size());
  }

}
