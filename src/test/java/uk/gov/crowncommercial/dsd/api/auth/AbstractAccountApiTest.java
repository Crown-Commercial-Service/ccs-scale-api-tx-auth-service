package uk.gov.crowncommercial.dsd.api.auth;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import io.restassured.RestAssured;

abstract class AbstractAccountApiTest extends AbstractApiTest {

  @Value("${api.paths.base.catalog}")
  String apiCatalogBasePath;

  @Value("${spree.api.paths.base.storefront}")
  String spreeApiCatalogBasePath;

  @Value("${api.paths.account}")
  String apiAccount;

  @Value("${spree.api.paths.account}")
  String spreeApiPathAccount;

  @Override
  @BeforeEach
  public void beforeEach() {
    super.beforeEach();
    RestAssured.basePath = apiCatalogBasePath;
  }

}
