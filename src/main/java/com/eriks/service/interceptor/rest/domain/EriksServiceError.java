package com.eriks.service.interceptor.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EriksServiceError {

  private int status;
    private String code;
    private String message;

}
