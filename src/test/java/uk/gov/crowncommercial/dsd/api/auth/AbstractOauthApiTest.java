package uk.gov.crowncommercial.dsd.api.auth;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import io.restassured.RestAssured;

abstract class AbstractOauthApiTest extends AbstractApiTest {

  @Value("${api.paths.base.oauth}")
  String apiOauthBasePath;

  @Value("${spree.api.paths.base.oauth}")
  String spreeApiOauthBasePath;

  @Override
  @BeforeEach
  public void beforeEach() {
    super.beforeEach();
    RestAssured.basePath = apiOauthBasePath;
  }

}
