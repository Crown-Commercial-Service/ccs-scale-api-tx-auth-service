package uk.gov.crowncommercial.dsd.api.auth.converter;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.crowncommercial.dsd.api.auth.model.account.AccountResponse;
import uk.gov.crowncommercial.dsd.api.auth.model.account.Address;
import uk.gov.crowncommercial.dsd.api.auth.model.spree.account.Account;

@SpringBootTest
@ActiveProfiles("test")
public class AccountConverterTest {

  private static String ACCOUNT_TEST_DATA = "test-spree-account-response.json";

  @Autowired
  AccountConverter accountConverter;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testToAccountResponse() throws Exception {

    AccountResponse response = accountConverter.toAccountResponse(getTestSpreeAccount());
    Address address = response.getAddresses().get(0);
    assertEquals("1", address.getId());
    assertEquals("spree@example.com", response.getEmail());
    assertEquals("Eddie", address.getFirstName());
    assertEquals("Ed", address.getLastName());
    assertEquals("22 Accacia Avenue", address.getAddress1());
    assertEquals("The place", address.getAddress2());
    assertEquals("Utopia", address.getCity());
    assertEquals("123456", address.getPhone());
    assertEquals("Buckinghamshire", address.getCounty());
    assertEquals("AB12 3ED", address.getPostCode());
    assertEquals("United Kingdom", address.getCountryName());
    assertEquals("Maiden", address.getCompany());
    assertEquals("GBR", address.getCountryIso3());
    assertEquals(true, address.getDefaultBillingAddress());
    assertEquals(false, address.getDefaultShippingAddress());
  }

  private Account getTestSpreeAccount() throws Exception {
    return objectMapper.readValue(
        getClass().getClassLoader().getResourceAsStream(ACCOUNT_TEST_DATA), Account.class);
  }
}
