package uk.gov.crowncommercial.dsd.api.auth.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Singular;

/**
 * Errors
 */
@Builder
public class ApiErrors {

  @JsonProperty("errors")
  @Singular
  private final List<ApiError> errors;

}
