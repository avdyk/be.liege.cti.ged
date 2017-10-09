package be.liege.ged.apix.test;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.http.HttpStatus;
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
        log.debug("alfred.url: {}", alfredUrl);
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
        return args -> {
            restTemplate.getInterceptors().add(
                    new BasicAuthorizationInterceptor(alfredUsername, alfredPassword));
            final ObjectNode body = getAllCategoryMissionWithFacets();
            log.debug("query body: {}", body.toString());
            final ResponseEntity<ObjectNode> objectNodeResponseEntity =
                    restTemplate.postForEntity(alfredUrl + "/v1/search", body, ObjectNode.class);
            if (objectNodeResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                final ObjectNode bodyResponse = objectNodeResponseEntity.getBody();
                final JsonNode nodeRefs = bodyResponse.get("noderefs");
                nodeRefs.elements()
                        .forEachRemaining(node -> {
                            log.info("{}: {}", node, getNodeName(restTemplate, node.asText()));
                        });
            } else {
                log.warn("Problème pour retrouver les informations: {}", objectNodeResponseEntity.getStatusCode());
            }
        };
    }

    private String getNodeName(final RestTemplate restTemplate, final String node) {
        final String[] splitColons = node.split(":");
        // space
        final String space = splitColons[0];
        final String[] splitSlashes = splitColons[1].substring(2).split("/");
        // store
        final String store = splitSlashes[0];
        // guid
        final String guid = splitSlashes[1];
        log.debug("node: {}; space: {}; store: {}; guid: {}", node, space, store, guid);
        final String queryUrl = String.format("%s/v1/nodes/%s/%s/%s", alfredUrl, space, store, guid);
        log.debug("Requête pour retrouver les noeuds: {}", queryUrl);
        final ObjectNode response = restTemplate.getForObject(queryUrl, ObjectNode.class);
        final JsonNode metadata = response.get("metadata");
        final JsonNode properties = metadata.get("properties");
        final JsonNode propertyNames = properties.get("{http://www.alfresco.org/model/content/1.0}name").get(0);
        log.debug("Nom du noeud: {}", propertyNames.asText());
        return propertyNames.asText();
    }

    private ObjectNode getAllCategoryMission() {
        final ObjectNode body = JsonNodeFactory.instance.objectNode();
        final ObjectNode query = JsonNodeFactory.instance.objectNode();
        query.put("path", "/cm:categoryRoot/vdl:vdlmission/*");
        body.putPOJO("query", query);
        return body;
    }

    private ObjectNode getAllCategoryMissionWithFacets() {
        final ObjectNode body = JsonNodeFactory.instance.objectNode();
        final ObjectNode query = JsonNodeFactory.instance.objectNode();
        query.put("path", "/cm:categoryRoot/vdl:vdlmission/*");
        body.putPOJO("query", query);
        final ObjectNode facets = JsonNodeFactory.instance.objectNode();
        facets.put("enabled", true);
        body.putPOJO("facets", facets);
        return body;
    }

    private ObjectNode getTenNodes() {
        final ObjectNode body = JsonNodeFactory.instance.objectNode();
        final ObjectNode query = JsonNodeFactory.instance.objectNode();
        query.put("type", "{http://www.alfresco.org/model/content/1.0}content");
        body.putPOJO("query", query);
        final ObjectNode paging = JsonNodeFactory.instance.objectNode();
        paging.put("limit", 10);
        paging.put("skip", 0);
        body.putPOJO("paging", paging);
        final ObjectNode facets = JsonNodeFactory.instance.objectNode();
        facets.put("enabled", false);
        body.putPOJO("facets", facets);
        return body;
    }

}
