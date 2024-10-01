package project.backend.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  private static final String BEARER_TOKEN_PREFIX = "Bearer";

  @Bean
  public OpenAPI openAPI() {
    String securityJwtName = "JWT";
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityJwtName);
    Components components = new Components()
        .addSecuritySchemes(securityJwtName, new SecurityScheme()
            .name(securityJwtName)
            .type(SecurityScheme.Type.HTTP)
            .scheme(BEARER_TOKEN_PREFIX)
            .bearerFormat(securityJwtName));

    return new OpenAPI()
        .info(apiInfo())
        .addSecurityItem(securityRequirement)
        .components(components);
  }

  private Info apiInfo() {
    return new Info()
        .title("Someup API Page") // API의 제목
        .description("스위프 6기 5팀 API") // API에 대한 설명
        .version("1.0.0"); // API의 버전
  }
}