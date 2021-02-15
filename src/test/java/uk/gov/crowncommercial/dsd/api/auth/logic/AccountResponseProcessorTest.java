package uk.gov.crowncommercial.dsd.api.auth.logic;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dsd.api.auth.converter.AccountConverter;
import uk.gov.crowncommercial.dsd.api.auth.model.account.AccountResponse;
import uk.gov.crowncommercial.dsd.api.auth.model.spree.account.Account;

@SpringBootTest
@ActiveProfiles("test")
public class AccountResponseProcessorTest {

  @Autowired
  private AccountResponseProcessor accountResponseProcessor;

  @MockBean
  private AccountConverter mockAccountConverter;

  @MockBean
  private AccountResponse mockAccountResponse;

  @MockBean
  private Exchange mockExchange;

  @MockBean
  private Message mockMessage;

  @Test
  public void processTest() throws Exception {

    Account mockAccount = getMockAccount();

    when(mockExchange.getIn()).thenReturn(mockMessage);
    when(mockMessage.getBody(Account.class)).thenReturn(mockAccount);
    when(mockAccountConverter.toAccountResponse(mockAccount)).thenReturn(mockAccountResponse);

    accountResponseProcessor.process(mockExchange);

    verify(mockMessage, times(1)).setBody(mockAccountResponse);
  }

  /*
   * Cannot use @MockBean as final class
   */
  private Account getMockAccount() {
    return new Account();
  }
}
