package uk.gov.crowncommercial.dsd.api.auth;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import uk.gov.crowncommercial.dsd.api.auth.routes.AuthServiceRouteBuilder;

@Ignore
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@MockEndpointsAndSkip("http://spree-host")
// @AutoConfigureMockMvc
@TestPropertySource(properties = {"SPREE_API_HOST=http://spree-host",})
public class AuthServiceAPITest {

  @Value("${api.paths.base}")
  private String apiBasePath;

  @Value("${api.paths.get-token}")
  private String apiGetToken;

  @LocalServerPort
  private int port;

  @Autowired
  private CamelContext camelContext;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
    RestAssured.basePath = apiBasePath;
  }

  @Test
  public void getToken() throws Exception {

    // Mock the behaviour of the Spree v2 API
    AdviceWith.adviceWith(camelContext, AuthServiceRouteBuilder.ROUTE_ID_GET_TOKEN, builder -> {
      builder
          .weaveByToUri("http://spree-host/spree_oauth/token?httpMethod=POST&bridgeEndpoint=true")
          .replace().setBody()
          .constant("{\"access_token\": \"VjEs16MG7SGmXXyurcGTiVC1Q2Ui7jRRlcLiRgtmC-A\","
              + "\"token_type\": \"Bearer\",\"expires_in\": 7200,"
              + "\"refresh_token\": \"OZGndgBdsBPhShM-Liy-YsImh7Mld6sEU3NzqtdXKO4\","
              + "\"created_at\": 1612328775}");
    });

    // @formatter:off
    given()
    .when()
      .post(apiGetToken)
    .then()
      .statusCode(SC_OK)
      .contentType(ContentType.JSON)
      .body("access_token", is("VjEs16MG7SGmXXyurcGTiVC1Q2Ui7jRRlcLiRgtmC-A"))
      .body("token_type", is("Bearer"))
      .body("expires_in", is(7200))
      .body("refresh_token", is("OZGndgBdsBPhShM-Liy-YsImh7Mld6sEU3NzqtdXKO4"))
      .body("created_at", is(1612328775));
     // @formatter:on
  }

}
