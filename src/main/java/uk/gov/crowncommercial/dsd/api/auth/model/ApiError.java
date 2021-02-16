package uk.gov.crowncommercial.dsd.api.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

/**
 * ApiError
 */
@Value
public class ApiError {

  @JsonProperty("status")
  String status;

  @JsonProperty("title")
  String title;

  @JsonProperty("detail")
  String detail;
}
