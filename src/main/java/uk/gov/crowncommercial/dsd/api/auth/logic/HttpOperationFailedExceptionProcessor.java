package uk.gov.crowncommercial.dsd.api.auth.logic;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.crowncommercial.dsd.api.auth.model.ApiError;
import uk.gov.crowncommercial.dsd.api.auth.model.ApiErrors;
import uk.gov.crowncommercial.dsd.api.auth.model.spree.SpreeError;

/**
 * Processes exceptions returned from Spree end point. At present it will extract the Spree error
 * code and message and construct an error from those details to return to client.
 *
 */
@Component
public class HttpOperationFailedExceptionProcessor implements Processor {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public void process(Exchange exchange) throws Exception {

    HttpOperationFailedException caused =
        exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);

    final SpreeError error = objectMapper.readValue(caused.getResponseBody(), SpreeError.class);

    exchange.getIn()
        .setBody(ApiErrors.builder()
            .error(
                new ApiError(caused.getStatusText(), error.getError(), error.getErrorDescription()))
            .build());

    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, caused.getStatusCode());
  }

}
