package uk.gov.crowncommercial.dsd.api.auth.routes;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dsd.api.auth.converter.AccountConverter;
import uk.gov.crowncommercial.dsd.api.auth.logic.HttpOperationFailedExceptionProcessor;
import uk.gov.crowncommercial.dsd.api.auth.logic.TokenRequestValidator;
import uk.gov.crowncommercial.dsd.api.auth.model.ApiError;
import uk.gov.crowncommercial.dsd.api.auth.model.ApiErrors;
import uk.gov.crowncommercial.dsd.api.auth.model.spree.account.Account;
import uk.gov.crowncommercial.dsd.api.auth.model.token.TokenResponse;

/**
 * Authorisation Service RouteBuilder.
 * 
 * For now includes both /token and /account end points.
 */
@Component
@RequiredArgsConstructor
public class AuthServiceRouteBuilder extends EndpointRouteBuilder {

  private static final String SPREE_DUMMY_ENDPOINT =
      "http://spree-api?headerFilterStrategy=#spreeApiHeaderFilter";

  private static final String DEBUG_LOG_MSG = "log:DEBUG?showBody=false&showHeaders=true";

  private static final String DIRECT = "direct:";

  public static final String ROUTE_ID_GET_TOKEN = "get-token";
  private static final String ROUTE_GET_TOKEN = DIRECT + ROUTE_ID_GET_TOKEN;

  public static final String ROUTE_ID_GET_ACCOUNT = "get-account";
  private static final String ROUTE_GET_ACCOUNT = DIRECT + ROUTE_ID_GET_ACCOUNT;

  public static final String ROUTE_ID_CREATE_ACCOUNT = "create-account";
  private static final String ROUTE_CREATE_ACCOUNT = DIRECT + ROUTE_ID_CREATE_ACCOUNT;

  public static final String ROUTE_ID_UPDATE_ACCOUNT = "update-account";
  private static final String ROUTE_UPDATE_ACCOUNT = DIRECT + ROUTE_ID_UPDATE_ACCOUNT;

  private static final String ROUTE_FINALISE_RESPONSE = "direct:finalise-response";

  @Value("${api.paths.base.oauth}")
  private String apiOauthBasePath;

  @Value("${api.paths.base.catalog}")
  private String apiCatalogBasePath;

  @Value("${api.paths.token}")
  private String apiPathToken;

  @Value("${api.paths.account}")
  private String apiPathAccount;

  @Value("${SPREE_API_HOST}")
  private String spreeApiHost;

  @Autowired
  AccountConverter accountConverter;

  @Autowired
  HttpOperationFailedExceptionProcessor httpExceptionProcessor;

  @Autowired
  private TokenRequestValidator tokenRequestValidator;

  @Override
  public void configure() throws Exception {

    // @formatter:off
    restConfiguration()
    .component("servlet")
    .bindingMode(RestBindingMode.json)
    .clientRequestValidation(true)
    .enableCORS(true);
    
    /*
     * Exception Handling
     */
    onException(HttpOperationFailedException.class)
    .handled(true)
    .process(httpExceptionProcessor);
    //.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500));

    onException(Exception.class)
    .handled(true)
    .logHandled(true)
    .setBody(constant(ApiErrors.builder().error(new ApiError(INTERNAL_SERVER_ERROR.name(), INTERNAL_SERVER_ERROR.getReasonPhrase(), "Unexpected error")).build()))
    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(INTERNAL_SERVER_ERROR.value()))
    .to(ROUTE_FINALISE_RESPONSE);
    
    /*
     * Get/Refresh Token
     */
    rest(apiOauthBasePath)
      .post(apiPathToken)
      .to(ROUTE_GET_TOKEN);

    from(ROUTE_GET_TOKEN)
      .routeId(ROUTE_ID_GET_TOKEN)
      .streamCaching()
      // Validate request
      .process(tokenRequestValidator)
      .log(LoggingLevel.INFO, "Endpoint get-token invoked")
      .marshal().json()
      .setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.POST))
      .setHeader(Exchange.HTTP_URI, simple("{{SPREE_API_HOST}}{{spree.api.paths.base.oauth}}{{spree.api.paths.token}}"))
      .to(DEBUG_LOG_MSG)
      .to(SPREE_DUMMY_ENDPOINT)
      .unmarshal(new JacksonDataFormat(TokenResponse.class))
      .to(ROUTE_FINALISE_RESPONSE);
    
    /*
     * Get Account
     */
    rest(apiCatalogBasePath)
      .get(apiPathAccount)
      .to(ROUTE_GET_ACCOUNT);

    from(ROUTE_GET_ACCOUNT)
      .routeId(ROUTE_ID_GET_ACCOUNT)
      //.streamCaching()
      .log(LoggingLevel.INFO, "Endpoint get-account invoked")
      .setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.GET))
      .setHeader(Exchange.HTTP_URI, simple("{{SPREE_API_HOST}}{{spree.api.paths.base.storefront}}{{spree.api.paths.account}}?include=default_billing_address&default_shipping_address"))
      .to(DEBUG_LOG_MSG)
      .to(SPREE_DUMMY_ENDPOINT)
      .unmarshal(new JacksonDataFormat(Account.class))
      .process(new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
          Account payload = exchange.getIn().getBody(Account.class);
          exchange.getIn().setBody(accountConverter.createFrom(payload));
        }
      })
      .to(ROUTE_FINALISE_RESPONSE);
    
    /*
     * Create Account
     */
    rest(apiCatalogBasePath)
      .post(apiPathAccount)
      .to(ROUTE_CREATE_ACCOUNT);

    from(ROUTE_CREATE_ACCOUNT)
      .routeId(ROUTE_ID_CREATE_ACCOUNT)
      .streamCaching()
      .log(LoggingLevel.INFO, "Endpoint create-account invoked")
      .log(LoggingLevel.INFO, "${body}")
      .setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.POST))
      .marshal().json(JsonLibrary.Jackson)
      .setHeader(Exchange.HTTP_URI, simple("{{SPREE_API_HOST}}{{spree.api.paths.base.storefront}}{{spree.api.paths.account}}"))
      .to(DEBUG_LOG_MSG)
      .to(SPREE_DUMMY_ENDPOINT)
      .unmarshal(new JacksonDataFormat(Account.class))
      .process(new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
          Account payload = exchange.getIn().getBody(Account.class);
          exchange.getIn().setBody(accountConverter.createFrom(payload));
        }
      })
      .to(ROUTE_FINALISE_RESPONSE);
    
    /*
     * Update Account
     */
    rest(apiCatalogBasePath)
      .patch(apiPathAccount)
      .to(ROUTE_UPDATE_ACCOUNT);

    from(ROUTE_UPDATE_ACCOUNT)
      .routeId(ROUTE_ID_UPDATE_ACCOUNT)
      .streamCaching()
      .log(LoggingLevel.INFO, "Endpoint update-account invoked")
      .setHeader(Exchange.CONTENT_TYPE, simple(MediaType.APPLICATION_JSON_VALUE))
      .setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.PATCH))
      .marshal().json(JsonLibrary.Jackson)
      .setHeader(Exchange.HTTP_URI, simple("{{SPREE_API_HOST}}{{spree.api.paths.base.storefront}}{{spree.api.paths.account}}"))
      .to(DEBUG_LOG_MSG)
      .to(SPREE_DUMMY_ENDPOINT)
      .unmarshal(new JacksonDataFormat(Account.class))
      .process(new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
          Account payload = exchange.getIn().getBody(Account.class);
          exchange.getIn().setBody(accountConverter.createFrom(payload));
        }
      })
      .to(ROUTE_FINALISE_RESPONSE);

    /*
     * Finalise Response
     */
    from(ROUTE_FINALISE_RESPONSE)
      .removeHeaders("*")
      .setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, constant("*"));
    
    // @formatter:on
  }

}
