package uk.gov.crowncommercial.dsd.api.auth.logic;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dsd.api.auth.converter.AccountConverter;
import uk.gov.crowncommercial.dsd.api.auth.model.spree.account.Account;

@Component
public class AccountResponseProcessor implements Processor {

  @Autowired
  AccountConverter accountConverter;

  @Override
  public void process(Exchange exchange) throws Exception {
    Account payload = exchange.getIn().getBody(Account.class);
    exchange.getIn().setBody(accountConverter.createFrom(payload));
  }

}
