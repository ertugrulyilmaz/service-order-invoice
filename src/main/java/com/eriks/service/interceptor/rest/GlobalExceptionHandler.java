package com.eriks.service.interceptor.rest;

import com.eriks.service.interceptor.annotation.DisableGlobalExceptionHandling;
import com.eriks.service.interceptor.exception.EriksServiceException;
import com.eriks.service.interceptor.rest.domain.EriksServiceError;
import com.eriks.service.interceptor.rest.domain.EriksServiceResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletionException;

@ControllerAdvice
@ConditionalOnMissingBean(annotation = DisableGlobalExceptionHandling.class)
public class GlobalExceptionHandler {

  @ExceptionHandler({EriksServiceException.class})
  public ResponseEntity<EriksServiceResponse> handleServiceException(EriksServiceException ex, HttpServletRequest request) {
    final EriksServiceError error = new EriksServiceError(ex.getStatus().value(), ex.getCode(), ex.getMessage());
    final EriksServiceResponse bundleResponse = new EriksServiceResponse(null, error, request.getRequestURI());

    return new ResponseEntity<>(bundleResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({CompletionException.class})
  public ResponseEntity<EriksServiceResponse> handleCompletionException(final Exception ex, final HttpServletRequest request) {
    EriksServiceError error;
    HttpStatus status;

    if (ex.getCause() instanceof EriksServiceException) {
      final EriksServiceException serviceException = (EriksServiceException) ex.getCause();

      status = serviceException.getStatus();
      error = new EriksServiceError(status.value(), serviceException.getCode(), serviceException.getMessage());
    } else {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
      error = new EriksServiceError(status.value(), "INTERNAL_SERVER_ERROR", ex.getMessage());
    }

    final EriksServiceResponse serviceResponse = new EriksServiceResponse(null, error, request.getRequestURI());
    return new ResponseEntity<>(serviceResponse, status);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<EriksServiceResponse> handleException(Exception ex, HttpServletRequest request) {
    final EriksServiceError error = new EriksServiceError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR", ex.getMessage());
    final EriksServiceResponse serviceResponse = new EriksServiceResponse(null, error, request.getRequestURI());

    return new ResponseEntity<>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
