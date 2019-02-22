package com.eriks.service.interceptor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EriksServiceException extends BaseException {

  public EriksServiceException(final HttpStatus status, final String code, final String message, final Exception e) {
    super(status, code, message, e);
  }

  public EriksServiceException(final HttpStatus status, final String message, final Exception e) {
    this(status, null, message, e);
  }

  public EriksServiceException(final HttpStatus status, final String code, final String message) {
    this(status, code, message, null);
  }

  public EriksServiceException(final HttpStatus status, final String message) {
    this(status, message, null, null);
  }

  public EriksServiceException(final HttpStatus status, final Exception e) {
    this(status, e.getMessage(), e);
  }

}
