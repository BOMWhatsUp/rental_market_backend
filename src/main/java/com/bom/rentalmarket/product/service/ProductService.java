package com.bom.rentalmarket.product.service;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.model.CreateProductForm;
import com.bom.rentalmarket.product.repository.ProductRepository;
import com.bom.rentalmarket.product.type.StatusType;
import com.bom.rentalmarket.s3.S3Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

  public ProductBoard createRental(CreateProductForm form, MultipartFile[] imageFiles)
      throws IOException {
    List<String> imageUrls = new ArrayList<>();
    for (MultipartFile file : imageFiles) {
      String imageUrl = s3Service.upload(file);
      imageUrls.add(imageUrl);
    }

    ProductBoard productBoard = modelMapper.typeMap(CreateProductForm.class, ProductBoard.class)
        .addMapping(CreateProductForm::getTitle, ProductBoard::setTitle)
        .addMapping(CreateProductForm::getContents, ProductBoard::setContents)
        .addMapping(CreateProductForm::getUnitPrice, ProductBoard::setUnitPrice)
        .addMapping(CreateProductForm::getRegion, ProductBoard::setRegion)
        .addMapping(CreateProductForm::getCategoryName, ProductBoard::setCategoryName)
        .addMapping(CreateProductForm::getMaxPeriod, ProductBoard::setMaxPeriod)
        .addMapping(CreateProductForm::getSellerId, ProductBoard::setSellerId)
        .addMappings(mapper -> mapper.skip(ProductBoard::setId))
        .addMappings(mapper -> mapper.skip(ProductBoard::setModifiedAt))
        .map(form);

    productBoard.setCreatedAt(LocalDateTime.now());
    productBoard.setImageUrls(imageUrls);
    productBoard.setStatus(StatusType.AVAILABLE);

    return productRepository.save(productBoard);
  }

}