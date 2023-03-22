package com.bom.rentalmarket.product.service;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.model.CreateProductForm;
import com.bom.rentalmarket.product.model.GetProductForm;
import com.bom.rentalmarket.product.repository.ProductRepository;
import com.bom.rentalmarket.product.type.StatusType;
import com.bom.rentalmarket.s3.S3Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final S3Service s3Service;
  private final ModelMapper modelMapper;

  public ProductBoard createProduct(CreateProductForm form, List<MultipartFile> imageFiles,
      int mainImageIndex)
      throws IOException {
    List<String> imageUrls = new ArrayList<>();
    for (MultipartFile file : imageFiles) {
      String imageUrl = s3Service.upload(file);
      imageUrls.add(imageUrl);
    }

    ProductBoard productBoard = modelMapper.typeMap(CreateProductForm.class, ProductBoard.class)
        .addMapping(CreateProductForm::getTitle, ProductBoard::setTitle)
        .addMapping(CreateProductForm::getContent, ProductBoard::setContent)
        .addMapping(CreateProductForm::getUnitPrice, ProductBoard::setUnitPrice)
        .addMapping(CreateProductForm::getWishRegion, ProductBoard::setWishRegion)
        .addMapping(CreateProductForm::getCategoryName, ProductBoard::setCategoryName)
        .addMapping(CreateProductForm::getMaxRentalPeriod, ProductBoard::setMaxRentalPeriod)
        .addMapping(CreateProductForm::getSellerId, ProductBoard::setSellerId)
        .addMapping(CreateProductForm::getNickname, ProductBoard::setNickname)
        .addMappings(mapper -> mapper.skip(ProductBoard::setId))
        .map(form);

    productBoard.setCreatedAt(LocalDateTime.now());
    productBoard.setModifiedAt(LocalDateTime.now());
    productBoard.setImageUrls(imageUrls);
    productBoard.setStatus(StatusType.AVAILABLE);

    //대표이미지 추가 부분
    if (mainImageIndex >= 0 && mainImageIndex < imageUrls.size()) {
      String mainImageUrl = imageUrls.get(mainImageIndex);
      productBoard.setMainImageUrl(mainImageUrl);
    }

    return productRepository.save(productBoard);
  }

  public List<GetProductForm> getProducts() {
    List<ProductBoard> products = productRepository.findAll();

    return products.stream()
        .map(GetProductForm::from)
        .collect(Collectors.toList());
  }

}

