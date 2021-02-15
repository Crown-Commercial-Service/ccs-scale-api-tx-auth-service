package uk.gov.crowncommercial.dsd.api.auth.converter;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.camel.TypeConverters;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dsd.api.auth.model.account.AccountResponse;
import uk.gov.crowncommercial.dsd.api.auth.model.account.Address;
import uk.gov.crowncommercial.dsd.api.auth.model.spree.account.Account;
import uk.gov.crowncommercial.dsd.api.auth.model.spree.account.AddressAttributes;

/**
 * Converts the Account response from Spree into the CCS Account response.
 */
@Component
public class AccountConverter implements TypeConverters {

  public AccountResponse toAccountResponse(final Account payload) {

    AccountResponse response = new AccountResponse();
    response.setEmail(payload.getData().getAttributes().getEmail());

    if (payload.getIncluded() != null) {
      List<Address> addresses = payload.getIncluded().stream().map(i -> {

        AddressAttributes atts = i.getAttributes();
        Address address = new Address();
        address.setId(i.getId());
        address.setFirstName(atts.getFirstName());
        address.setLastName(atts.getLastName());
        address.setAddress1(atts.getAddress1());
        address.setAddress2(atts.getAddress2());
        address.setCity(atts.getCity());
        address.setPostCode(atts.getPostCode());
        address.setPhone(atts.getPhone());
        address.setCounty(atts.getCounty());
        address.setCountryName(atts.getCountryName());
        address.setCountryIso3(atts.getCountryIso3());
        address.setCompany(atts.getCompany());

        address.setDefaultBillingAddress(
            payload.getData().getRelationships().getDefaultBillingAddress().getData() != null
                && payload.getData().getRelationships().getDefaultBillingAddress().getData().getId()
                    .equals(i.getId()));

        address.setDefaultShippingAddress(
            payload.getData().getRelationships().getDefaultShippingAddress().getData() != null
                && payload.getData().getRelationships().getDefaultShippingAddress().getData()
                    .getId().equals(i.getId()));

        return address;

      }).collect(Collectors.toList());

      if (!addresses.isEmpty()) {
        response.setAddresses(addresses);
      }
    }

    return response;
  }
}
