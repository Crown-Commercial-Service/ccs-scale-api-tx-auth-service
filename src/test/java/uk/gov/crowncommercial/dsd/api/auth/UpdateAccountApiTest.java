package uk.gov.crowncommercial.dsd.api.auth;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.patchRequestedFor;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import io.restassured.http.ContentType;

class UpdateAccountApiTest extends AbstractAccountApiTest {


  static final String UPDATE_ACCOUNT_BODY_REQUEST = "{\"user\": {\"last_name\": \"Walker7\"}}";

  @Value("${api.paths.account}")
  private String apiGetAccount;

  @Value("${spree.api.paths.account}")
  private String spreeApiPathGetAccount;

  @Test
  void updateAccountAuthorised() throws Exception {

    final UriComponentsBuilder uriBuilder =
        UriComponentsBuilder.fromUriString(spreeApiCatalogBasePath + spreeApiPathGetAccount);
    final String spreeUri = uriBuilder.build().toString();

    stubFor(
        patch(urlEqualTo(spreeUri)).withRequestBody(equalToJson(UPDATE_ACCOUNT_BODY_REQUEST))
            .willReturn(aResponse().withStatus(200)
                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile("getAccountAuthorised.json")));

    final NotifyBuilder notifyBuilder = new NotifyBuilder(camelContext).whenDone(1).create();

    /*
     * Update account, all attributes tested (note: very limited response specified in CCS API
     * currently)
     */
    // @formatter:off
    given()
      .header(AUTHORIZATION, AUTH_BEARER_TOKEN)
      .body(UPDATE_ACCOUNT_BODY_REQUEST)
    .when()
      .patch(apiGetAccount)
    .then()
      .statusCode(SC_OK)
      .contentType(ContentType.JSON)
      .body("email", is("spree@example.com"));
    // @formatter:on

    // Assert exchange done before verifying external API call
    assertTrue(notifyBuilder.matches(5, TimeUnit.SECONDS));

    verify(1,
        patchRequestedFor(urlEqualTo(spreeUri))
            .withHeader(ACCEPT, equalTo(MediaType.APPLICATION_JSON_VALUE))
            .withHeader(AUTHORIZATION, equalTo(AUTH_BEARER_TOKEN)));
  }

}
