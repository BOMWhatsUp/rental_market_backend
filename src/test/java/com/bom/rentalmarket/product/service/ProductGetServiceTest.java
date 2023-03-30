package com.bom.rentalmarket.product.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bom.rentalmarket.product.model.GetProductForm;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.MaxPeriodType;
import com.bom.rentalmarket.product.type.StatusType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
public class ProductGetServiceTest {

  @Autowired
  private ProductService productService;


  @Test
  @Sql(scripts = {"classpath:schema.sql",
      "classpath:data.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  public void testGetProducts() {
    // given
    CategoryType categoryType = CategoryType.CLOTHING;
    StatusType statusType = StatusType.AVAILABLE;
    String keyword = "title";
    String userRegion = "서울시 용산구";
    int pageNo = 1;
    int pageSize = 10;

    List<GetProductForm> productList = productService.getProducts(categoryType, statusType, keyword,
        pageNo, pageSize, userRegion);

    // then
    assertNotNull(productList); // products map 객체가 null이 아닌지 확인
    assertEquals(3, productList.size()); // productList의 크기가 3인지 확인 - 더미 데이터는 위의 조건은 3개임

    assertEquals("test1@naver.com", productList.get(0).getSellerId());
    assertEquals("test12", productList.get(0).getNickname());
    assertEquals("title", productList.get(0).getTitle());
    assertEquals("content", productList.get(0).getContent());
    assertEquals(600L, productList.get(0).getUnitPrice());
    assertEquals("998f15eb-c565-4f87-a", productList.get(0).getMainImageUrl());
    assertEquals(MaxPeriodType.ONEMONTH, productList.get(0).getMaxRentalPeriod());
    assertEquals(StatusType.AVAILABLE, productList.get(0).getStatus());
    assertEquals("서울시 중랑구", productList.get(0).getWishRegion());
  }
}
