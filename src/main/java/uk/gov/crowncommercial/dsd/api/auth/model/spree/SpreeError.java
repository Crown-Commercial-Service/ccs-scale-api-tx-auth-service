package uk.gov.crowncommercial.dsd.api.auth.model.spree;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Spree Error.
 *
 */
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpreeError {

  @JsonProperty("error")
  String error;

  @JsonProperty("error_description")
  String errorDescription;

}
