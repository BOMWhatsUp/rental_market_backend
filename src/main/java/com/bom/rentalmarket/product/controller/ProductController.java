package com.bom.rentalmarket.product.controller;

import com.bom.rentalmarket.product.model.CreateProductForm;
import com.bom.rentalmarket.product.model.GetProductDetailForm;
import com.bom.rentalmarket.product.model.GetProductForm;
import com.bom.rentalmarket.product.service.ProductService;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.StatusType;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<List<GetProductForm>> getProducts(
      @RequestParam(value = "category-name", required = false) CategoryType categoryName,
      @RequestParam(required = false) StatusType status,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    List<GetProductForm> productList = productService.getProducts(categoryName, status, keyword,
        (long) page, (long) size);
    return ResponseEntity.ok().body(productList);
  }

  @GetMapping("/{productId}/detail")
  public ResponseEntity<GetProductDetailForm> getDetailProduct(
      @PathVariable Long productId) {
    GetProductDetailForm product = productService.getDetailProduct(productId);
    return ResponseEntity.ok().body(product);
  }


//   @GetMapping("/create")
//   public String getCreateProduct() {

//     return "create";
//   }

  @PostMapping("/create")
  public ResponseEntity<CreateProductForm> createProduct(
      @ModelAttribute CreateProductForm createProductForm,
      @RequestParam("imageFiles") List<MultipartFile> imageFiles,
      @RequestParam("mainImageIndex") int mainImageIndex) throws IOException {

    CreateProductForm addProduct = productService.createProduct(createProductForm, imageFiles,
        mainImageIndex);
    return ResponseEntity.ok(addProduct);
  }
}
