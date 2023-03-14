package com.bom.rentalmarket.product.type;

public enum MaxPeriodType {
    ONEMONTH("1개월"),
    TWOMONTH("1개월"),
    THREEMONTH("1개월");


    private final String value;

    MaxPeriodType(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
}
