package uk.gov.crowncommercial.dsd.api.auth;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
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

class GetTokenApiTest extends AbstractOauthApiTest {

  private static final String GET_TOKEN_BODY_REQUEST =
      "{\"grant_type\": \"password\",\"username\": \"Jack\",\"password\": \"Daniels\"}";

  @Value("${api.paths.token}")
  private String apiGetToken;

  @Value("${spree.api.paths.token}")
  private String spreeApiPathGetToken;

  @Test
  void getToken() throws Exception {

    final UriComponentsBuilder uriBuilder =
        UriComponentsBuilder.fromUriString(spreeApiOauthBasePath + spreeApiPathGetToken);
    final String spreeUri = uriBuilder.build().toString();

    stubFor(
        post(urlEqualTo(spreeUri)).withRequestBody(equalToJson(GET_TOKEN_BODY_REQUEST))
            .willReturn(aResponse().withStatus(200)
                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile("getAccessToken.json")));

    final NotifyBuilder notifyBuilder = new NotifyBuilder(camelContext).whenDone(1).create();

    /*
     * Get (POST) token, all attributes tested
     */
    // @formatter:off
    given()
      .header(AUTHORIZATION, AUTH_BEARER_TOKEN)
      .body(GET_TOKEN_BODY_REQUEST)
    .when()
      .post(apiGetToken)
    .then()
      .statusCode(SC_OK)
      .contentType(ContentType.JSON)
      .body("access_token", is("hzu2D060lVen4YvvRGZqxMlxAhhg0AE2ambsY7PeNSc"))
      .body("token_type", is("Bearer"))
      .body("refresh_token", is("BkDlx7RvG4rNHQZ9FjuS2gnl63D7l4DbthKY3ge21sc"))
      .body("expires_in", is(7200))
      .body("created_at", is(1614591663));
    // @formatter:on

    // Assert exchange done before verifying external API call
    assertTrue(notifyBuilder.matches(5, TimeUnit.SECONDS));

    verify(1, postRequestedFor(urlEqualTo(spreeUri)).withHeader(ACCEPT,
        equalTo(MediaType.APPLICATION_JSON_VALUE)));
  }

}
