package be.liege.ged.apix.test;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SearchExampleApp {

    private static final Logger log = LoggerFactory.getLogger(SearchExampleApp.class);

    private final String alfredUrl;
    private final String alfredUsername;
    private final String alfredPassword;

    public SearchExampleApp(@Value(AlfredProperties.ALFRESCO_URL) final String alfredUrl,
                            @Value(AlfredProperties.ALFRESCO_USERNAME) final String alfredUsername,
                            @Value(AlfredProperties.ALFRESCO_PASSWORD) final String alfredPassword) {
        log.info("alfred.url: {}", alfredUrl);
        this.alfredUrl = alfredUrl;
        this.alfredUsername = alfredUsername;
        this.alfredPassword = alfredPassword;
    }


    public static void main(String args[]) {
        SpringApplication.run(SearchExampleApp.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        restTemplate.getInterceptors().add(
                new BasicAuthorizationInterceptor(alfredUsername, alfredPassword));

        return args -> {
            final ObjectNode body = JsonNodeFactory.instance.objectNode();
            final ObjectNode query = JsonNodeFactory.instance.objectNode();
            query.put("path", "/cm:categoryRoot/vdl:vdlmission/*");
            body.putPOJO("query", query);

            log.info("query body: {}", body.toString());
            final ResponseEntity<ObjectNode> objectNodeResponseEntity =
                    restTemplate.postForEntity(alfredUrl + "/search", body, ObjectNode.class);
            log.info(objectNodeResponseEntity.toString());
        };
    }
}
