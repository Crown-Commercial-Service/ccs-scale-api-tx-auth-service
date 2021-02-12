package uk.gov.crowncommercial.dsd.api.auth.model.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
// @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
// @AllArgsConstructor
public class TokenRequest {

  @JsonProperty("access_token")
  String accessToken;

  @JsonProperty("token_type")
  String tokenType;

  @JsonProperty("expires_in")
  Integer expiresIn;

  @JsonProperty("refresh_token")
  String refreshToken;

  @JsonProperty("created_at_X")
  Integer createdAtX;

}
