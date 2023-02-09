package com.develonity.common.exception;

public class NotEnoughStockException extends IllegalArgumentException {

  public NotEnoughStockException(String s) {
    super(s);
  }
}
