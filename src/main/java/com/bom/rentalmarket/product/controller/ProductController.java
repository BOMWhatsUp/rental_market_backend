package com.bom.rentalmarket.product.controller;

import com.bom.rentalmarket.product.exception.ProductControllerAdvice.NotFoundException;
import com.bom.rentalmarket.product.model.CreateProductForm;
import com.bom.rentalmarket.product.model.CreateRentalHistoryForm;
import com.bom.rentalmarket.product.model.GetProductDetailForm;
import com.bom.rentalmarket.product.model.GetProductForm;
import com.bom.rentalmarket.product.model.GetTransactionForm;
import com.bom.rentalmarket.product.service.ProductService;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.StatusType;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;


  @GetMapping
  public ResponseEntity<List<GetProductForm>> getProducts(
      @RequestParam(required = false) String userRegion,
      @RequestParam(value = "category-name", required = false) CategoryType categoryName,
      @RequestParam(required = false) StatusType status,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    try {
      List<GetProductForm> productList = productService.getProducts(categoryName, status, keyword,
          page, size, userRegion);
      return ResponseEntity.ok().body(productList);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @GetMapping("/{productId}/detail")
  public ResponseEntity<GetProductDetailForm> getProductDetail(
      @PathVariable Long productId) {
    GetProductDetailForm product = productService.getProductDetail(productId);
    return ResponseEntity.ok().body(product);
  }

  @GetMapping("/{productId}/transaction")
  public ResponseEntity<GetTransactionForm> getProductTransaction(
      @PathVariable Long productId) {
    GetTransactionForm product = productService.getProductTransaction(productId);
    return ResponseEntity.ok().body(product);
  }

  @PostMapping("/transaction/")
  public ResponseEntity<CreateRentalHistoryForm> createRentalHistory(
      @RequestParam String userId,
      @RequestParam Long productId,
      @RequestParam Long totalPrice,
      @RequestParam int days) {
    CreateRentalHistoryForm addHistory = productService.createRentalHistory(
        userId, productId, totalPrice, days);
    return ResponseEntity.ok(addHistory);
  }

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
