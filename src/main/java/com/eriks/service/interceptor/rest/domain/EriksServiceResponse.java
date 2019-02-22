package com.eriks.service.interceptor.rest.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
public class EriksServiceResponse {

    @JsonProperty(value = "data")
    private Object data;

    @JsonProperty(value = "error")
    private EriksServiceError error;

    @JsonProperty(value = "uri")
    private String uri;
}
