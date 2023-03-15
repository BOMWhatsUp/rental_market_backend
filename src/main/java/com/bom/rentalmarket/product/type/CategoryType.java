package com.bom.rentalmarket.product.type;

public enum CategoryType {
  CLOTHING("의류"),
  HOME("생활가전"),
  FURNITURE("가구/인테리어"),
  DIGITAL("디지털 기기"),
  BOOK("도서"),
  GAMEANDRECORD("게임/음반");

  private final String value;

  CategoryType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
