package uk.gov.crowncommercial.dsd.api.auth.logic;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dsd.api.auth.model.ApiError;
import uk.gov.crowncommercial.dsd.api.auth.model.ApiErrors;
import uk.gov.crowncommercial.dsd.api.auth.model.spree.SpreeError;

/**
 * Processes exceptions returned from Spree end point. At present it will extract the Spree error
 * code and message and construct an error from those details to return to client.
 *
 */
@Component
@Slf4j
public class HttpOperationFailedExceptionProcessor implements Processor {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public void process(Exchange exchange) throws Exception {

    HttpOperationFailedException caused =
        exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);

    try {
      // Parse the Spree response to extract meaningful response on what the issue may be
      final SpreeError error = objectMapper.readValue(caused.getResponseBody(), SpreeError.class);
      exchange.getIn()
          .setBody(ApiErrors.builder().error(
              new ApiError(caused.getStatusText(), error.getError(), error.getErrorDescription()))
              .build());

    } catch (Exception e) {
      // If Spree does not return expected JSON response, construct a generic error message
      log.error(
          "An unexpected error occurred calling the Spree API. Check Spree logs for more information.",
          e);
      exchange.getIn()
          .setBody(ApiErrors.builder()
              .error(new ApiError(INTERNAL_SERVER_ERROR.name(), "An unexpected error occurred",
                  "An unexpected error occurred calling internal catalog API"))
              .build());
    }

    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, caused.getStatusCode());
  }

}
