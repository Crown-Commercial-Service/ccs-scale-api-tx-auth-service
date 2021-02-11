package uk.gov.crowncommercial.dsd.api.auth.logic;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class TokenRequestValidator implements Processor {

  @Override
  public void process(final Exchange exchange) throws Exception {
    /*
     * TODO: Validate request... throw RequestValidationException or continue... etc
     */

    String payload = exchange.getIn().getBody(String.class);
    System.out.println(">>>> " + payload);
  }

}
