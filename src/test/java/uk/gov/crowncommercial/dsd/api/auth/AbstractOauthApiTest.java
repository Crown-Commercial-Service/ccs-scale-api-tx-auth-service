package uk.gov.crowncommercial.dsd.api.auth;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import io.restassured.RestAssured;

/**
 * Abstract superclass for testing '/oauth2' resources.
 *
 */
abstract class AbstractOauthApiTest extends AbstractApiTest {

  @Value("${api.paths.base.oauth}")
  String apiOauthBasePath;

  @Value("${spree.api.paths.base.oauth}")
  String spreeApiOauthBasePath;

  @Value("${api.paths.token}")
  String apiToken;

  @Value("${spree.api.paths.token}")
  String spreeApiPathToken;

  @Override
  @BeforeEach
  public void beforeEach() {
    super.beforeEach();
    RestAssured.basePath = apiOauthBasePath;
  }

}
