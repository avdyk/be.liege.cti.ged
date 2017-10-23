package be.liege.cti.ged.test.search;

import be.liege.cti.ged.api.AlfredConfiguration;
import be.liege.cti.ged.api.AlfredConstants;
import be.liege.cti.ged.api.AlfredServiceFactory;
import be.liege.cti.ged.api.NodeReference;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Import(AlfredConfiguration.class)
public class SearchCategories {

    private static final Logger logger = LoggerFactory.getLogger(SearchCategories.class);

    private final String alfredUrl;
    private final String alfredUsername;
    private final String alfredPassword;
    private final AlfredServiceFactory alfredServiceFactory;

    public SearchCategories(@Value(ApixTestProperties.ALFRESCO_URL) final String alfredUrl,
                            @Value(ApixTestProperties.ALFRESCO_USERNAME) final String alfredUsername,
                            @Value(ApixTestProperties.ALFRESCO_PASSWORD) final String alfredPassword,
                            final AlfredServiceFactory alfredServiceFactory) {
        this.alfredServiceFactory = alfredServiceFactory;
        logger.debug("alfred.url: {}", alfredUrl);
        this.alfredUrl = alfredUrl;
        this.alfredUsername = alfredUsername;
        this.alfredPassword = alfredPassword;
    }


    public static void main(String args[]) {
        SpringApplication.run(SearchCategories.class);
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
            logger.debug("query body: {}", body.toString());
            final ResponseEntity<ObjectNode> objectNodeResponseEntity =
                    restTemplate.postForEntity(alfredUrl + AlfredConstants.SEARCH, body, ObjectNode.class);
            if (objectNodeResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                final ObjectNode bodyResponse = objectNodeResponseEntity.getBody();
                final JsonNode nodeRefs = bodyResponse.get("noderefs");
                nodeRefs.elements()
                        .forEachRemaining(node -> {
                            logger.info("{}: {}", node, getNodeName(restTemplate, node.asText()));
                        });
            } else {
                logger.warn("Problème pour retrouver les informations: {}", objectNodeResponseEntity.getStatusCode());
            }
        };
    }

    private String getNodeName(final RestTemplate restTemplate, final String node) {
        final NodeReference nodeReference = alfredServiceFactory.nodeReferenceBuilder().nodeReference(node);
        logger.debug("node: {}; space: {}; store: {}; guid: {}", node, nodeReference.getSpace(),
                nodeReference.getStore(), nodeReference.getGuid());
        final String queryUrl = String.format("%s%s/%s/%s/%s", alfredUrl, AlfredConstants.NODES,
                nodeReference.getSpace(), nodeReference.getStore(), nodeReference.getGuid());
        logger.debug("Requête pour retrouver les noeuds: {}", queryUrl);
        final ObjectNode response = restTemplate.getForObject(queryUrl, ObjectNode.class);
        final JsonNode metadata = response.get("metadata");
        final JsonNode properties = metadata.get("properties");
        final JsonNode propertyNames = properties.get("{http://www.alfresco.org/model/content/1.0}name").get(0);
        logger.debug("Nom du noeud: {}", propertyNames.asText());
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
