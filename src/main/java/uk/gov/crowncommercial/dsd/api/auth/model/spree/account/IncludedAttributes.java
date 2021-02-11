package uk.gov.crowncommercial.dsd.api.auth.model.spree.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncludedAttributes {

  @JsonProperty("firstname")
  String firstName;

  @JsonProperty("lastname")
  String lastName;

  @JsonProperty("address1")
  String address1;

  @JsonProperty("address2")
  String address2;

  @JsonProperty("city")
  String city;

  @JsonProperty("zipcode")
  String postCode;

  @JsonProperty("phone")
  String phone;

  @JsonProperty("state_name")
  String county;

  @JsonProperty("company")
  String company;

  @JsonProperty("country_name")
  String countryName;

  @JsonProperty("country_iso3")
  String countryIso3;

  @JsonProperty("country_iso")
  String countryIso;

  @JsonProperty("state_code")
  String stateCode;
}
