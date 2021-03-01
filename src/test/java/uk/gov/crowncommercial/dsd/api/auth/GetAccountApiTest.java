package uk.gov.crowncommercial.dsd.api.auth;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import java.util.concurrent.TimeUnit;
import org.apache.camel.builder.NotifyBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import io.restassured.http.ContentType;

class GetAccountApiTest extends AbstractAccountApiTest {

  @Test
  void getAccountAuthorised() throws Exception {

    final UriComponentsBuilder uriBuilder =
        UriComponentsBuilder.fromUriString(spreeApiCatalogBasePath + spreeApiPathAccount
            + "?include=default_billing_address&default_shipping_address");
    final String spreeUri = uriBuilder.build().toString();

    stubFor(get(urlEqualTo(spreeUri)).willReturn(
        aResponse().withStatus(200).withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withBodyFile("getAccountAuthorised.json")));

    final NotifyBuilder notifyBuilder = new NotifyBuilder(camelContext).whenDone(1).create();

    /*
     * Get account, all attributes tested
     */
    // @formatter:off
    given()
      .header(AUTHORIZATION, AUTH_BEARER_TOKEN)
    .when()
      .get(apiAccount)
    .then()
      .statusCode(SC_OK)
      .contentType(ContentType.JSON)
      .body("email", is("spree@example.com"))
      .body("addresses[0].id", is("1"))
      .body("addresses[0].firstname", is("Eddie"))
      .body("addresses[0].lastname", is("Ed"))
      .body("addresses[0].address1", is("22 Acacia Avenue"))
      .body("addresses[0].address2", is("The place"))
      .body("addresses[0].city", is("Utopia"))
      .body("addresses[0].postcode", is("AB12 3ED"))
      .body("addresses[0].phone", is("0123 888666"))
      .body("addresses[0].county", is("Buckinghamshire"))
      .body("addresses[0].countryName", is("United Kingdom"))
      .body("addresses[0].countryIso3", is("GBR"))
      .body("addresses[0].company", is("Maiden"))
      .body("addresses[0].defaultBillingAddress", is(true))
      .body("addresses[0].defaultShippingAddress", is(false));
    // @formatter:on

    // Assert exchange done before verifying external API call
    assertTrue(notifyBuilder.matches(5, TimeUnit.SECONDS));

    verify(1,
        getRequestedFor(urlEqualTo(spreeUri))
            .withHeader(ACCEPT, equalTo(MediaType.APPLICATION_JSON_VALUE))
            .withHeader(AUTHORIZATION, equalTo(AUTH_BEARER_TOKEN)));
  }

}
