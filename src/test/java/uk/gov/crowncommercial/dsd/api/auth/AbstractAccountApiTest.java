package uk.gov.crowncommercial.dsd.api.auth;

import org.apache.camel.CamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext
@AutoConfigureWireMock(port = 0)
abstract class AbstractAccountApiTest {

  static final String AUTH_BEARER_TOKEN = "Bearer abc123";

  @Value("${api.paths.base.catalog}")
  String apiCatalogBasePath;

  @Value("${spree.api.paths.base.storefront}")
  String spreeApiCatalogBasePath;


  @Autowired
  CamelContext camelContext;

  @LocalServerPort
  int port;

  @BeforeEach
  public void beforeEach() {
    RestAssured.port = port;
    RestAssured.basePath = apiCatalogBasePath;
  }

  @AfterEach
  public void afterEach() {
    WireMock.reset();
  }

}
