package com.eriks.service.interceptor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private final HttpStatus status;
  private final String code;


  public BaseException(final HttpStatus status, final String code, final String message, final Exception e) {
    super(message, e);
    this.status = status;
    this.code = code;
  }
}
