package com.bom.rentalmarket.product.service;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.entity.QProductBoard;
import com.bom.rentalmarket.product.model.CreateProductForm;
import com.bom.rentalmarket.product.model.GetProductForm;
import com.bom.rentalmarket.product.repository.ProductRepository;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.StatusType;
import com.bom.rentalmarket.s3.S3Service;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final S3Service s3Service;
  private final JPAQueryFactory jpaQueryFactory;

  public ProductBoard createProduct(CreateProductForm form, List<MultipartFile> imageFiles,
      int mainImageIndex)
      throws IOException {
    List<String> imageUrls = new ArrayList<>();
    for (MultipartFile file : imageFiles) {
      String imageUrl = s3Service.upload(file);
      imageUrls.add(imageUrl);
    }

    ProductBoard productBoard = ProductBoard.builder()
        .title(form.getTitle())
        .content(form.getContent())
        .unitPrice(form.getUnitPrice())
        .wishRegion(form.getWishRegion())
        .categoryName(form.getCategoryName())
        .maxRentalPeriod(form.getMaxRentalPeriod())
        .sellerId(form.getSellerId())
        .nickname(form.getNickname())
        .createdAt(LocalDateTime.now())
        .modifiedAt(LocalDateTime.now())
        .imageUrls(imageUrls)
        .status(StatusType.AVAILABLE)
        .build();

    //대표이미지 추가 부분
    if (mainImageIndex >= 0 && mainImageIndex < imageUrls.size()) {
      String mainImageUrl = imageUrls.get(mainImageIndex);
      productBoard.setMainImageUrl(mainImageUrl);
    }

    return productRepository.save(productBoard);
  }

  public Map<String, Object> getProducts(CategoryType categoryName, StatusType status,
      String keyword, int pageNo, int pageSize) {
    QProductBoard productBoard = QProductBoard.productBoard;

    BooleanBuilder builder = new BooleanBuilder();

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

    List<ProductBoard> productBoardList = jpaQueryFactory.selectFrom(productBoard)
        .where(builder)
        .orderBy(productBoard.createdAt.desc())
        .offset((pageNo - 1) * pageSize)
        .limit(pageSize)
        .fetch();

    boolean hasNextPage = jpaQueryFactory.selectFrom(productBoard)
        .where(builder)
        .offset(pageNo * pageSize)
        .limit(1)
        .fetch()
        .isEmpty();

    Map<String, Object> result = new HashMap<>();
    result.put("productList",
        productBoardList.stream().map(GetProductForm::from).collect(Collectors.toList()));
    result.put("hasNextPage", !hasNextPage);

    return result;
  }

}

