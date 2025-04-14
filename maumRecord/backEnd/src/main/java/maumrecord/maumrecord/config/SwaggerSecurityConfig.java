package maumrecord.maumrecord.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@SecurityScheme(
        name = "bearerAuth", // Swagger에서 참조할 이름
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT" // 문서상 표시용, 없어도 무방
)
public class SwaggerSecurityConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                )
                .security(Collections.singletonList(
                        new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth")
                ));
    }
}
