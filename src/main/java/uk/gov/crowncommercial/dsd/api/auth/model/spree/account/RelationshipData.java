package uk.gov.crowncommercial.dsd.api.auth.model.spree.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Spree Relationship Data.
 *
 */
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipData {

  @JsonProperty("id")
  String id;

  @JsonProperty("type")
  String type;

}
