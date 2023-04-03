package com.bom.rentalmarket.product.controller;

import com.bom.rentalmarket.product.model.CreateProductForm;
import com.bom.rentalmarket.product.model.CreateRentalHistoryForm;
import com.bom.rentalmarket.product.model.GetProductDetailForm;
import com.bom.rentalmarket.product.model.GetProductForm;
import com.bom.rentalmarket.product.model.GetRentalHistoryForm;
import com.bom.rentalmarket.product.model.GetTransactionForm;
import com.bom.rentalmarket.product.service.ProductService;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.StatusType;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RestController
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @RestController
  public class HealthController {
    @GetMapping(value = "/")
    public ResponseEntity<String> healthCheck() {
      return ResponseEntity.ok().build();
    }
  }

  @PostMapping("/product")
  public ResponseEntity<CreateProductForm> createProduct(
      @ModelAttribute CreateProductForm createProductForm,
      @RequestParam("imageFiles") List<MultipartFile> imageFiles,
      @RequestParam("mainImageIndex") int mainImageIndex) throws IOException {

    CreateProductForm addProduct = productService.createProduct(createProductForm, imageFiles,
        mainImageIndex);
    return ResponseEntity.ok(addProduct);
  }

  @GetMapping("/products")
  public ResponseEntity<List<GetProductForm>> getProducts(
      @RequestParam(required = false) String userRegion,
      @RequestParam(value = "category-name", required = false) CategoryType categoryName,
      @RequestParam(required = false) StatusType status,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {

    List<GetProductForm> productList = productService.getProducts(categoryName, status, keyword,
        (long) page, (long) size, userRegion);

    return ResponseEntity.ok().body(productList);

  }

  @GetMapping("/product/{id}")
  public ResponseEntity<GetProductDetailForm> getProductDetail(
      @PathVariable Long id) {
    GetProductDetailForm product = productService.getProductDetail(id);
    return ResponseEntity.ok().body(product);
  }

  @GetMapping("/payment/product/{id}")
  public ResponseEntity<GetTransactionForm> getProductTransaction(
      @PathVariable Long id) {
    GetTransactionForm product = productService.getProductTransaction(id);
    return ResponseEntity.ok().body(product);
  }


  @PostMapping("/payment/product/{id}")
  public ResponseEntity<CreateRentalHistoryForm> createRentalHistory(
      @PathVariable Long id,
      @RequestBody CreateRentalHistoryForm createRentalHistoryForm) {

    CreateRentalHistoryForm addHistory = productService.createRentalHistory(id, createRentalHistoryForm);
    return ResponseEntity.ok(addHistory);
  }

  @GetMapping("/history/{userId}/buyer")
  public ResponseEntity<List<GetRentalHistoryForm>> getBuyerRentalHistory(
      @PathVariable String userId,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {

    List<GetRentalHistoryForm> productList = productService.getBuyerRentalHistory(userId,
        page, size);

    return ResponseEntity.ok().body(productList);
  }

  @GetMapping("/history/{sellerId}/seller")
  public ResponseEntity<List<GetRentalHistoryForm>> getSellerRentalHistory(
      @PathVariable String sellerId,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {

    List<GetRentalHistoryForm> productList = productService.getSellerRentalHistory(sellerId,
        page, size);

    return ResponseEntity.ok().body(productList);
  }

  @DeleteMapping("/history/{id}")
  public ResponseEntity<String> deleteBuyerRentalHistory(
      @PathVariable Long id) {
    productService.deleteHistory(id);
    return ResponseEntity.ok("요청하신 내역이 삭제되었습니다.");
  }

  @PutMapping("/rental/{id}")
  public ResponseEntity<String> returnComplete(
      @PathVariable Long id) {
    productService.rentalReturnComplete(id);
    return ResponseEntity.ok("요청하신 반납처리가 완료되었습니다.");
  }

  //  @DeleteMapping("/delete")
//  public ResponseEntity<String> deleteProduct(
//      @RequestParam Long productId,
//      @RequestParam String userId) {
//    productService.deleteProduct(productId, userId);
//    return ResponseEntity.ok("요청하신 게시물이 삭제되었습니다.");
//  }

}
