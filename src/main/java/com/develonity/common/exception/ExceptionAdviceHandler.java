package com.develonity.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdviceHandler {


  @ExceptionHandler({CustomException.class})
  protected ResponseEntity handleCustomException(CustomException ex) {
    return new ResponseEntity(
        new ErrorDto(ex.getExceptionStatus().getStatusCode(), ex.getExceptionStatus().getMessage()),
        HttpStatus.valueOf(ex.getExceptionStatus().getStatusCode()));
  }


  @ExceptionHandler({MethodArgumentNotValidException.class})
  protected ResponseEntity handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    return new ResponseEntity<>(e.getBindingResult().getFieldErrors().get(0).getField() + "의 "
        + e.getBindingResult().getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler({RuntimeException.class})
  protected ResponseEntity<String> handleEtcException(RuntimeException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
