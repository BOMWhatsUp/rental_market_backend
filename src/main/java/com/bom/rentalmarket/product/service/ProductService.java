package com.bom.rentalmarket.product.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.bom.rentalmarket.UserController.domain.model.entity.Member;
import com.bom.rentalmarket.UserController.repository.MemberRepository;
import com.bom.rentalmarket.chatting.domain.chat.ChatMessageForm;
import com.bom.rentalmarket.chatting.service.ChatRoomService;
import com.bom.rentalmarket.chatting.service.ChatService;
import com.bom.rentalmarket.product.entity.ProductBoard;
import com.bom.rentalmarket.product.entity.QRentalHistory;
import com.bom.rentalmarket.product.entity.RentalHistory;
import com.bom.rentalmarket.product.model.CreateProductForm;
import com.bom.rentalmarket.product.model.CreateRentalHistoryForm;
import com.bom.rentalmarket.product.model.GetProductDetailForm;
import com.bom.rentalmarket.product.model.GetProductForm;
import com.bom.rentalmarket.product.model.GetRentalHistoryForm;
import com.bom.rentalmarket.product.model.GetTransactionForm;
import com.bom.rentalmarket.product.model.RentalHistoryDetail;
import com.bom.rentalmarket.product.repository.ProductRepository;
import com.bom.rentalmarket.product.repository.RentalHistoryRepository;
import com.bom.rentalmarket.product.type.CategoryType;
import com.bom.rentalmarket.product.type.StatusType;
import com.bom.rentalmarket.s3.S3Service;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final RentalHistoryRepository rentalHistoryRepository;
  private final S3Service s3Service;
  private final ChatRoomService chatRoomService;
  private final MemberRepository memberRepository;
  private final EntityManager entityManager;
  private final ChatService chatService;

  public CreateProductForm createProduct(CreateProductForm form, List<MultipartFile> imageFiles,
      int mainImageIndex)
      throws IOException {

    Member optionalSeller = memberRepository.findByEmail(form.getSellerId())
        .orElseThrow(() -> new IllegalArgumentException("잘못된 판매자 아이디 입니다."));

    // 이미지 파일의 확장자를 체크하여 유효한 파일만 걸러내고, S3에 업로드합니다.
    List<String> imageUrls = new ArrayList<>();
    String mainImageUrl = "";
    for (MultipartFile file : imageFiles) {
      if (isValidImageExtension(file)) {
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
        .sellerId(optionalSeller)
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
        .sellerId(optionalSeller.getEmail())
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
      String keyword, Long pageNo, Long pageSize, String userRegion) {

    List<ProductBoard> productBoardList = productRepository.searchFilters(categoryName,
        status, keyword, pageNo, pageSize, userRegion);

    return productBoardList.stream().map(productBoard -> {
      LocalDateTime returnDate = null;
      if (productBoard.getStatus() == StatusType.RENTED) {
        Optional<RentalHistory> optionalRentalHistory = rentalHistoryRepository.findByProductIdAndSellerIdIsNotNullAndReturnYnFalse(
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
    Member sellerId = productBoard.getSellerId();

    Optional<RentalHistory> optionalRentalHistory = rentalHistoryRepository.findByProductIdAndSellerIdIsNotNullAndReturnYnFalse(
        productBoard);

    LocalDateTime returnDate = optionalRentalHistory.map(RentalHistory::getReturnDate).orElse(null);

    String sellerRegion = sellerId.getRegion();
    String sellerProfile = sellerId.getImageUrl();

    return GetProductDetailForm.from(productBoard, returnDate, sellerRegion, sellerProfile);
  }

  public GetTransactionForm getProductTransaction(Long productId) {

    Optional<ProductBoard> optionalProductBoard = productRepository.findById(productId);

    if (optionalProductBoard.isEmpty()) {
      throw new NoSuchElementException("Product with PRODUCT_ID" + productId);
    }

    ProductBoard productBoard = optionalProductBoard.get();

    return GetTransactionForm.from(productBoard);
  }

  @Transactional
  public CreateRentalHistoryForm createRentalHistory(Long prId, CreateRentalHistoryForm form) {

    String userId = form.getUserId();
    String userNickName = form.getUserNickName();
    Long totalPrice = form.getTotalPrice();
    int days = form.getDays();

    Optional<ProductBoard> product = productRepository.findById(prId);
    if (product.isEmpty()) {
      throw new NoSuchElementException("Product with PRODUCT_ID" + prId);
    }
    Optional<Member> optionalMember = memberRepository.findByEmail(userId);
    if (optionalMember.isEmpty()) {
      throw new NoSuchElementException("Product with USER_ID" + userId);
    }
    ProductBoard productBoard = product.get();

    Member sellerId = productBoard.getSellerId();
    String userProfile = optionalMember.get().getImageUrl();
    String sellerNickName = productBoard.getNickname();

    if (sellerId == null) {
      throw new RuntimeException("Product with id " + productBoard.getId() + " 가 존재하지 않습니다.");
    }

    //Product Status 변경
    productBoard.setRentStatus(StatusType.RENTED);
    productRepository.save(productBoard);

    RentalHistory userRentalHistory = RentalHistory.builder()
        .productId(productBoard)
        .content(productBoard.getContent())
        .title(productBoard.getTitle())
        .status(productBoard.getStatus())
        .mainImageUrl(productBoard.getMainImageUrl())
        .categoryName(productBoard.getCategoryName())
        .userId(userId)
        .sellerId(null)
        .sellerProfile(sellerId.getImageUrl())
        .sellerNickName(sellerId.getNickName())
        .totalPrice(totalPrice)
        .rentalDate(LocalDateTime.now())
        .returnDate(LocalDateTime.now().plusDays(days))
        .returnYn(false)
        .build();

    RentalHistory sellerRentalHistory = RentalHistory.builder()
        .productId(productBoard)
        .content(productBoard.getContent())
        .title(productBoard.getTitle())
        .status(productBoard.getStatus())
        .mainImageUrl(productBoard.getMainImageUrl())
        .categoryName(productBoard.getCategoryName())
        .userId(null)
        .sellerId(sellerId.getEmail())
        .sellerProfile(sellerId.getImageUrl())
        .sellerNickName(sellerId.getNickName())
        .totalPrice(totalPrice)
        .rentalDate(LocalDateTime.now())
        .returnDate(LocalDateTime.now().plusDays(days))
        .returnYn(false)
        .build();

    rentalHistoryRepository.save(sellerRentalHistory);
    RentalHistory saveUserRentalHistory = rentalHistoryRepository.save(userRentalHistory);

    Long roomId = chatRoomService.connectRoomBetweenUsers(userNickName, sellerNickName,
        productBoard);

    String message = String.format(
        "%s 님이 상품 렌탈을 요청하셨습니다."
            + "렌탈 요청 정보\n렌탈 희망 기간 : %s ~ %s\n합계 금액 : %d 입니다.",
        userId, saveUserRentalHistory.getRentalDate().toLocalDate(),
        saveUserRentalHistory.getReturnDate().toLocalDate(), totalPrice);

    ChatMessageForm chatForm = ChatMessageForm.builder()
        .roomId(roomId)
        .sender(userNickName)
        .receiver(sellerNickName)
        .userProfile(userProfile)
        .message(message)
        .build();

    chatService.saveMessage(chatForm);

    return CreateRentalHistoryForm.builder()
        .totalPrice(saveUserRentalHistory.getTotalPrice())
        .returnDate(saveUserRentalHistory.getReturnDate())
        .roomId(roomId)
        .build();
  }

  // 기획 마지막 단계에서 적용 (상품 수정, 상품 제거 카드)
//  public void deleteProduct(Long productId, String sellerId) {
//
//    Optional<ProductBoard> optionalProductBoard = productRepository.findByIdAndSellerId(productId,
//        sellerId);
//    if (optionalProductBoard.isPresent()) {
//      ProductBoard productBoard = optionalProductBoard.get();
//
//      String mainImageUrl = productBoard.getMainImageUrl();
//      List<String> imageUrls = productBoard.getImageUrls();
//
//      productRepository.delete(productBoard);
//
//      s3Service.deleteImageUrls(imageUrls, mainImageUrl);
//    }
//  }
  public List<GetRentalHistoryForm> getRentalHistory(String userId, String role, Long page,
      Long size) {
    QRentalHistory qRentalHistory = QRentalHistory.rentalHistory;
    BooleanExpression userIdEq;
    BooleanExpression buyerIdNotNull = qRentalHistory.userId.isNotNull();
    BooleanExpression sellerIdNotNull = qRentalHistory.sellerId.isNotNull();

    if ("buyer".equals(role)) {
      userIdEq = qRentalHistory.userId.eq(userId).and(buyerIdNotNull);
    } else if ("seller".equals(role)) {
      userIdEq = qRentalHistory.sellerId.eq(userId).and(sellerIdNotNull);
    } else {
      throw new IllegalArgumentException("Unknown role: " + role);
    }

    JPAQuery<RentalHistory> query = new JPAQuery<>(entityManager);
    List<RentalHistory> rentalHistoryList = query.select(qRentalHistory)
        .from(qRentalHistory)
        .where(userIdEq)
        .orderBy(qRentalHistory.rentalDate.desc())
        .offset((page - 1) * size)
        .limit(size)
        .fetch();

    return rentalHistoryList.stream()
        .map(GetRentalHistoryForm::from)
        .collect(Collectors.toList());
  }

  public List<GetRentalHistoryForm> getBuyerRentalHistory(String userId, Long page, Long size) {

    String buyer = "buyer";

    return getRentalHistory(userId, buyer, page, size);
  }

  public List<GetRentalHistoryForm> getSellerRentalHistory(String sellerId, Long page, Long size) {

    String seller = "seller";
    return getRentalHistory(sellerId, seller, page, size);
  }

  public void deleteHistory(Long id) {
    Optional<RentalHistory> optionalRentalHistory = rentalHistoryRepository.findById(id);
    if (optionalRentalHistory.isPresent()) {
      rentalHistoryRepository.deleteById(id);
    } else {
      throw new NotFoundException("대여 기록을 찾을 수 없습니다.");
    }
  }


  public void rentalReturnComplete(Long id) {

    RentalHistory rentalHistory = rentalHistoryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("대여 기록을 찾을 수 없습니다."));

    rentalHistory.setStatusAndReturnYn(StatusType.RETURNED, true);

    ProductBoard productBoard = rentalHistory.getProductId();
    productBoard.setRentStatus(StatusType.AVAILABLE);

    rentalHistoryRepository.save(rentalHistory);

  }

  public RentalHistoryDetail rentalHistoryDetail(Long id) {

    RentalHistory rentalHistory = rentalHistoryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("대여 기록을 찾을 수 없습니다."));

    return RentalHistoryDetail.from(rentalHistory);
  }
}