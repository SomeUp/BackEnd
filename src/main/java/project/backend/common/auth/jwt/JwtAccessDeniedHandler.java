package project.backend.common.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  private static final String EXCEPTION_ACCESS_HANDLER = "/exception/access-denied";

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    if (!request.isSecure()) {
      String redirectUrl = "https://" + request.getServerName() + EXCEPTION_ACCESS_HANDLER;
      response.sendRedirect(redirectUrl);
      return;
    }
    response.sendRedirect(EXCEPTION_ACCESS_HANDLER);
  }
}
