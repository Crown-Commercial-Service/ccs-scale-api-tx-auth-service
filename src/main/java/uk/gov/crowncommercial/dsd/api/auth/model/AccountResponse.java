package uk.gov.crowncommercial.dsd.api.auth.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * CCS Account.
 *
 */
@Data
@JsonPropertyOrder({"email", "addresses"})
public class AccountResponse {

  @JsonProperty("email")
  String email;

  List<Address> addresses;

}
