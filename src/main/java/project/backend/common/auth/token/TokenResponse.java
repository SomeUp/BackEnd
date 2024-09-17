package project.backend.common.auth.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenResponse {

  private final String accessToken;

  @JsonIgnore
  private final String refreshToken;

  public static TokenResponse of(final String accessToken, final String refreshToken) {
    return new TokenResponse(accessToken, refreshToken);
  }
}
