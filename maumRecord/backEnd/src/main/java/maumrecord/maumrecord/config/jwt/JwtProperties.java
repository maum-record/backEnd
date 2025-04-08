package maumrecord.maumrecord.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@Configuration("jwt")
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
