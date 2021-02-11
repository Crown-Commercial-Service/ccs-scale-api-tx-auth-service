package uk.gov.crowncommercial.dsd.api.auth.model.spree.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Spree Account Relationships.
 *
 */
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Relationships {

  @JsonProperty("default_billing_address")
  RelationshipDataWrapper defaultBillingAddress;

  @JsonProperty("default_shipping_address")
  RelationshipDataWrapper defaultShippingAddress;

}
