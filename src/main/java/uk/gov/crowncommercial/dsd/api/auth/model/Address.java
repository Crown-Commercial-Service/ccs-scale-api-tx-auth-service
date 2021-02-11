package uk.gov.crowncommercial.dsd.api.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Address {

  @JsonProperty("id")
  String id;

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

  @JsonProperty("postcode")
  String postCode;

  @JsonProperty("phone")
  String phone;

  @JsonProperty("county")
  String county;

  @JsonProperty("countryName")
  String countryName;

  @JsonProperty("countryIso3")
  String countryIso3;

  @JsonProperty("company")
  String company;

  @JsonProperty("defaultBillingAddress")
  Boolean defaultBillingAddress;

  @JsonProperty("defaultShippingAddress")
  Boolean defaultShippingAddress;
}

