package com.bom.rentalmarket.product.type;

public enum StatusType {
  AVAILABLE("대여가능"),
  RENTED("대여중"),
  WAITING("반납대기"),
  RETURNED("반납완료");

  private final String value;

  StatusType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
