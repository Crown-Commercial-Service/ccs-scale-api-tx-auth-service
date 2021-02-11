package uk.gov.crowncommercial.dsd.api.auth.model.spree.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Spree Included (expect only 'address' types here).
 *
 */
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Included {

  @JsonProperty("id")
  String id;

  @JsonProperty("type")
  String type;

  @JsonProperty("attributes")
  AddressAttributes attributes;
}
