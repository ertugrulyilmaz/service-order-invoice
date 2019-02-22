package com.eriks.service.config.model;

import lombok.Getter;

@Getter
public class SecurityProperties {

  private final int jwtAuthExpMinute;
  private final String jwtAuthSecret;

  public SecurityProperties(final String jwtAuthExpMinute, final String jwtAuthSecret) {
    this.jwtAuthExpMinute = parseInt(jwtAuthExpMinute);
    this.jwtAuthSecret = jwtAuthSecret;
  }

  private int parseInt(String val) {
    return Integer.valueOf(val);
  }
}
