package com.bom.rentalmarket.product.controller;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.model.CreateProductForm;
import com.bom.rentalmarket.product.service.ProductService;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.StatusType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;


  @GetMapping
  public ResponseEntity<Map<String, Object>> getProducts(
      @RequestParam(value = "category-name", required = false) CategoryType categoryName,
      @RequestParam(required = false) StatusType status,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    Map<String, Object> productList = productService.getProducts(categoryName, status, keyword, page, size);
    return ResponseEntity.ok().body(productList);
  }

  @GetMapping("/create")
  public String getCreateProduct() {

    return "create";
  }

  @PostMapping("/create")
  public ResponseEntity<String> createProduct(
      @ModelAttribute CreateProductForm createProductForm,
      @RequestParam("imageFiles") MultipartFile[] imageFiles,
      @RequestParam("mainImageIndex") int mainImageIndex) throws IOException {

    List<MultipartFile> imageFilesOnly = new ArrayList<>();
    for (MultipartFile file : imageFiles) {
      String fileType = file.getContentType();
      String extension = "";
      if (fileType != null) {
        extension = fileType.substring(fileType.indexOf('/') + 1);
      } else {
        extension = file.getOriginalFilename()
            .substring(file.getOriginalFilename().lastIndexOf('.') + 1);
      }
      if (extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png")
          || extension.equals("gif")) {
        imageFilesOnly.add(file);
      }
    }
    ProductBoard rental = productService.createProduct(createProductForm, imageFilesOnly,
        mainImageIndex);
    return ResponseEntity.ok("상품이 등록되었습니다.");
  }
}
