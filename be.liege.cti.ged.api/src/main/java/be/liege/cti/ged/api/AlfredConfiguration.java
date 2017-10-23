package be.liege.cti.ged.api;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan({AlfredConstants.PACKAGE_API})
public class AlfredConfiguration {

    @Bean
    @Profile("!dev")
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

}
