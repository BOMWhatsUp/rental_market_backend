package com.bom.rentalmarket.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductControllerAdvice {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다.");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(CreateProductException.class)
  public ResponseEntity<ErrorResponse> handleCreateProductException(CreateProductException e) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "상품 등록 중 오류가 발생하였습니다.");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(CreateRentalHistoryException.class)
  public ResponseEntity<ErrorResponse> handleCreateRentalHistoryException(
      CreateRentalHistoryException e) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "대여 내역 생성 중 오류가 발생하였습니다.");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생하였습니다."));
  }


  public static class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
      super(message);
    }
  }

  public static class CreateProductException extends RuntimeException {

    public CreateProductException(String message) {
      super(message);
    }
  }

  public static class CreateRentalHistoryException extends RuntimeException {

    public CreateRentalHistoryException(String message) {
      super(message);
    }
  }


}
