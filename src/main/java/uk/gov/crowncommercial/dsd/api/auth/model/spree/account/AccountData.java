package uk.gov.crowncommercial.dsd.api.auth.model.spree.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Spree Account Data.
 *
 */
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountData {

  @JsonProperty("id")
  String id;

  @JsonProperty("type")
  String type;

  @JsonProperty("attributes")
  UserAttributes attributes;

  @JsonProperty("relationships")
  Relationships relationships;
}
