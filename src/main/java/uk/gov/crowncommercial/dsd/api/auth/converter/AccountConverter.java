package uk.gov.crowncommercial.dsd.api.auth.converter;

import java.util.ArrayList;
import org.apache.camel.TypeConverters;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dsd.api.auth.model.AccountResponse;
import uk.gov.crowncommercial.dsd.api.auth.model.Address;
import uk.gov.crowncommercial.dsd.api.auth.model.spree.account.Included;
import uk.gov.crowncommercial.dsd.api.auth.model.spree.account.IncludedAttributes;
import uk.gov.crowncommercial.dsd.api.auth.model.spree.account.SpreeAccount;

@Component
public class AccountConverter implements TypeConverters {


  public AccountResponse createFrom(final SpreeAccount payload) {

    AccountResponse response = new AccountResponse();
    response.setEmail(payload.getData().getAttributes().getEmail());
    response.setAddresses(new ArrayList<Address>());

    if (payload.getIncluded() != null) {
      for (Included included : payload.getIncluded()) {

        IncludedAttributes atts = included.getAttributes();

        Address address = new Address();

        address.setId(included.getId());
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

        if (payload.getData().getRelationships().getDefaultBillingAddress().getData() != null
            && payload.getData().getRelationships().getDefaultBillingAddress().getData().getId()
                .equals(included.getId())) {
          address.setDefaultBillingAddress(true);
        } else {
          address.setDefaultBillingAddress(false);
        }

        if (payload.getData().getRelationships().getDefaultShippingAddress().getData() != null
            && payload.getData().getRelationships().getDefaultShippingAddress().getData().getId()
                .equals(included.getId())) {
          address.setDefaultShippingAddress(true);
        } else {
          address.setDefaultShippingAddress(false);
        }

        response.getAddresses().add(address);
      }
    }

    return response;

  }
}
