package be.liege.cti.ged.api.impl;

import be.liege.cti.ged.api.AlfredConstants;
import be.liege.cti.ged.api.AlfredDocument;
import be.liege.cti.ged.api.AlfredPath;
import be.liege.cti.ged.api.AlfredService;
import be.liege.cti.ged.api.NodeReference;
import be.liege.cti.ged.api.NodeReferenceBuilder;
import be.liege.cti.ged.api.PathNotFoundException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class AlfredServiceImpl implements AlfredService {

    private static final Logger logger = LoggerFactory.getLogger(AlfredServiceImpl.class);

    private final NodeReferenceBuilder nodeReferenceBuilder;
    private final String url;
    private final RestTemplate restTemplate;

    AlfredServiceImpl(final RestTemplateBuilder restTemplateBuilder,
                      final NodeReferenceBuilder nodeReferenceBuilder,
                      final String url,
                      final String username,
                      final String password) {
        this.nodeReferenceBuilder = nodeReferenceBuilder;
        this.url = url;
        restTemplate = restTemplateBuilder.build();
        // restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getInterceptors().add(
                new BasicAuthorizationInterceptor(username, password != null ? password : ""));
        logger.debug("Creation of AlfredServiceImpl for URL {}", url);
    }

    AlfredServiceImpl(final RestTemplateBuilder restTemplateBuilder,
                      final NodeReferenceBuilder nodeReferenceBuilder,
                      final String url,
                      final String username) {
        this(restTemplateBuilder, nodeReferenceBuilder, url, username, null);
    }

    @Override
    public AlfredPath pathSearchByName(String path) throws PathNotFoundException {
        logger.debug("Search path: {}", path);
        final ObjectNode body = JsonNodeFactory.instance.objectNode();
        final ObjectNode query = JsonNodeFactory.instance.objectNode();
        query.put("path", path);
        body.putPOJO("query", query);
        final ResponseEntity<ObjectNode> objectNodeResponseEntity =
                restTemplate.postForEntity(url + AlfredConstants.SEARCH, body, ObjectNode.class);
        final AlfredPath alfredPath;
        if (objectNodeResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            final ObjectNode bodyResponse = objectNodeResponseEntity.getBody();
            final String nodeRef = bodyResponse.get("noderefs").get(0).asText();
            alfredPath = ()
                    -> name
                    -> type
                    -> new AlfredDocumentBuilderWithTNameAndNameAndTypeImpl(nodeReferenceBuilder, url, restTemplate, nodeRef, name, type);
            logger.debug("Found: {}", nodeRef);
        } else {
            throw new PathNotFoundException(path, objectNodeResponseEntity.getStatusCode());
        }
        return alfredPath;
    }

    @Override
    public AlfredDocument documentByNodeReference(NodeReference nodeReference) {
        return new AlfredDocumentImpl(url, restTemplate, nodeReference);
    }

    @Override
    public void deleteByNodeReference(NodeReference nodeReference) {
        final String restUrl = String.format("%s%s/%s/%s/%s",
                url, AlfredConstants.NODES, nodeReference.getSpace(), nodeReference.getStore(), nodeReference.getGuid());
        logger.debug("Suppression du noeud {}", restUrl);
        restTemplate.delete(restUrl);
    }

    @Override
    public String getUrl() {
        return url;
    }
}
