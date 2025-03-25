package maumrecord.maumrecord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@SpringBootApplication
public class MaumRecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaumRecordApplication.class, args);
    }

}
