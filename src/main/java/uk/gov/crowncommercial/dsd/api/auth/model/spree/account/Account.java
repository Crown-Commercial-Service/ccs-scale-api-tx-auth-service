package uk.gov.crowncommercial.dsd.api.auth.model.spree.account;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Spree Account.
 *
 */
@Value
@NoArgsConstructor(force = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

  @JsonProperty("data")
  AccountData data;

  @JsonProperty("included")
  List<Included> included;
}
