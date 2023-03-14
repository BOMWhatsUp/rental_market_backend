package com.bom.rentalmarket.product.controller;

import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.model.CreateProductForm;
import com.bom.rentalmarket.product.service.ProductService;
import java.io.IOException;
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
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public String readRental() {

    return "/rentals";
  }

  @PostMapping
  public ResponseEntity<ProductBoard> createRental(@ModelAttribute CreateProductForm createProductForm, @RequestParam("imageFiles") MultipartFile[] imageFiles) throws IOException {
    ProductBoard rental = productService.createRental(createProductForm, imageFiles);
    return ResponseEntity.ok(rental);
  }
}

//  @GetMapping("/category/{category}")
//  public List<RentalBoard> findByCategory(@PathVariable("category") CategoryType category) {
//    return rentalService.findByCategory(category);
//  }
