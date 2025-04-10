package maumrecord.maumrecord;

import maumrecord.maumrecord.config.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
public class MaumRecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaumRecordApplication.class, args);
    }

}
