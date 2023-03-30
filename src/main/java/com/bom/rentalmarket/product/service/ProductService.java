package com.bom.rentalmarket.product.service;

import com.bom.rentalmarket.UserController.domain.model.entity.Member;
import com.bom.rentalmarket.UserController.repository.MemberRepository;
import com.bom.rentalmarket.chatting.service.ChatRoomService;
import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.entity.RentalHistory;
import com.bom.rentalmarket.product.model.CreateProductForm;
import com.bom.rentalmarket.product.model.CreateRentalHistoryForm;
import com.bom.rentalmarket.product.model.GetProductDetailForm;
import com.bom.rentalmarket.product.model.GetProductForm;
import com.bom.rentalmarket.product.model.GetTransactionForm;
import com.bom.rentalmarket.product.repository.ProductRepository;
import com.bom.rentalmarket.product.repository.RentalHistoryRepository;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.StatusType;
import com.bom.rentalmarket.s3.S3Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final RentalHistoryRepository rentalHistoryRepository;
  private final MemberRepository memberRepository;
  private final S3Service s3Service;

  public CreateProductForm createProduct(CreateProductForm form, List<MultipartFile> imageFiles,
      int mainImageIndex)
      throws IOException {

    // 이미지 파일의 확장자를 체크하여 유효한 파일만 걸러내고, S3에 업로드합니다.
    List<MultipartFile> validImageFiles = new ArrayList<>();
    List<String> imageUrls = new ArrayList<>();
    String mainImageUrl = "";
    for (MultipartFile file : imageFiles) {
      if (isValidImageExtension(file)) {
        validImageFiles.add(file);
        String imageUrl = s3Service.upload(file);
        imageUrls.add(imageUrl);
      }
    }

    // 메인 이미지 URL을 구합니다.
    if (mainImageIndex >= 0 && mainImageIndex < imageUrls.size()) {
      mainImageUrl = imageUrls.get(mainImageIndex);
    }

    // ProductBoard 객체를 생성합니다.
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
        .mainImageUrl(mainImageUrl)
        .status(StatusType.AVAILABLE)
        .build();

    ProductBoard savedProduct = productRepository.save(productBoard);

    // ProductBoard 객체를 저장하고 CreateProductForm 객체로 변환하여 리턴합니다.
    return CreateProductForm.builder()
        .sellerId(savedProduct.getSellerId())
        .nickname(savedProduct.getNickname())
        .title(savedProduct.getTitle())
        .content(savedProduct.getContent())
        .imageUrls(savedProduct.getImageUrls())
        .mainImageUrl(savedProduct.getMainImageUrl())
        .wishRegion(savedProduct.getWishRegion())
        .maxRentalPeriod(savedProduct.getMaxRentalPeriod())
        .categoryName(savedProduct.getCategoryName())
        .unitPrice(savedProduct.getUnitPrice())
        .status(savedProduct.getStatus())
        .createdAt(savedProduct.getCreatedAt())
        .modifiedAt(savedProduct.getModifiedAt())
        .build();
  }

  private boolean isValidImageExtension(MultipartFile file) {
    String fileType = file.getContentType();
    String extension;
    if (fileType != null) {
      extension = fileType.substring(fileType.indexOf('/') + 1);
    } else {
      extension = file.getOriginalFilename()
          .substring(file.getOriginalFilename().lastIndexOf('.') + 1);
    }
    return extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png")
        || extension.equals("gif");
  }


  public List<GetProductForm> getProducts(CategoryType categoryName, StatusType status,
      String keyword, int pageNo, int pageSize, String userRegion) {

    List<ProductBoard> productBoardList = productRepository.searchFilters(categoryName,
        status, keyword, pageNo, pageSize, userRegion);

    return productBoardList.stream().map(productBoard -> {
      LocalDateTime returnDate = null;
      if (productBoard.getStatus() == StatusType.RENTED) {
        Optional<RentalHistory> optionalRentalHistory = rentalHistoryRepository.findByProductIdAndReturnYnFalse(
            productBoard);
        returnDate = optionalRentalHistory.map(RentalHistory::getReturnDate).orElse(null);
      }
      return GetProductForm.from(productBoard, returnDate);
    }).collect(Collectors.toList());
  }

  public GetProductDetailForm getProductDetail(Long productId) {

    Optional<ProductBoard> optionalProductBoard = productRepository.findById(productId);
    if (optionalProductBoard.isEmpty()) {
      throw new NoSuchElementException("Product with PRODUCT_ID" + productId);
    }

    ProductBoard productBoard = optionalProductBoard.get();
    String email = productBoard.getSellerId();

    Optional<RentalHistory> optionalRentalHistory = rentalHistoryRepository.findByProductIdAndReturnYnFalse(
        productBoard);

    LocalDateTime returnDate = optionalRentalHistory.map(RentalHistory::getReturnDate).orElse(null);

    Optional<Member> optionalMember = memberRepository.findByEmail(email);
    String sellerRegion = optionalMember.map(Member::getRegion).orElse(null);
    String sellerProfile = optionalMember.map(Member::getImageUrl).orElse(null);

    return GetProductDetailForm.from(productBoard, returnDate, sellerRegion, sellerProfile);
  }

  public GetTransactionForm getProductTransaction(Long productId) {

    Optional<ProductBoard> optionalProductBoard = productRepository.findById(productId);

    if (optionalProductBoard.isEmpty()) {
      throw new NoSuchElementException("Product with ID" + productId);
    }

    ProductBoard productBoard = optionalProductBoard.get();

    return GetTransactionForm.from(productBoard);
  }

  public CreateRentalHistoryForm createRentalHistory(
      CreateRentalHistoryForm createRentalHistoryForm) {
    return null;
  }
}

