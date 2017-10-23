package be.liege.ged.apix.vdl.impl;

import be.liege.ged.apix.vdl.api.AlfredConstants;
import be.liege.ged.apix.vdl.api.AlfredDocument;
import be.liege.ged.apix.vdl.api.AlfredDocumentBuilderWithNameAndType;
import be.liege.ged.apix.vdl.api.NodeReferenceBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

public class AlfredDocumentBuilderWithTNameAndNameAndTypeImpl implements AlfredDocumentBuilderWithNameAndType {

    private static final Logger logger = LoggerFactory.getLogger(AlfredDocumentBuilderWithTNameAndNameAndTypeImpl.class);

    private final NodeReferenceBuilder nodeReferenceBuilder;
    private final String url;
    private final RestTemplate restTemplate;
    private final String parentNodeRef;
    private final String name;
    private final String type;
    private Properties properties;

    AlfredDocumentBuilderWithTNameAndNameAndTypeImpl(final NodeReferenceBuilder nodeReferenceBuilder,
                                                     final String url,
                                                     final RestTemplate restTemplate,
                                                     final String parentNodeRef,
                                                     final String name,
                                                     final String type) {
        this.nodeReferenceBuilder = nodeReferenceBuilder;
        this.url = url;
        this.restTemplate = restTemplate;
        this.parentNodeRef = parentNodeRef;
        this.name = name;
        this.type = type;
    }

    @Override
    public AlfredDocumentBuilderWithNameAndType properties(Properties properties) {
        logger.debug("Adds {} properties", properties != null ? properties.size() : "NULL!");
        this.properties = properties;
        return this;
    }

    @Override
    public AlfredDocument create() {
        logger.debug("Create document: {} / {} of type: {}", parentNodeRef, name, type);
        final ObjectNode body = JsonNodeFactory.instance.objectNode();
        body.put("parent", parentNodeRef);
        body.put("name", name);
        body.put("type", type);
        if (properties != null) {
            ObjectMapper mapper = new ObjectMapper();
            final ObjectNode props = JsonNodeFactory.instance.objectNode();
            try {
                logger.debug("with properties: {}", properties);
                props.put("properties", mapper.writeValueAsString(properties));
            } catch (JsonProcessingException e) {
                logger.error("Unable to transform properties to json string: {}", properties);
                throw new RuntimeException("Unable to transform properties to json string", e);
            }
        }
        final String createDocumentUrl = url + AlfredConstants.NODES;
        logger.debug("rest call to node: {} with request: {}", createDocumentUrl, body);
        final ResponseEntity<ObjectNode> objectNodeResponseEntity =
                restTemplate.postForEntity(createDocumentUrl, body, ObjectNode.class);
        final AlfredDocument alfredDocument;
        if (objectNodeResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            final ObjectNode bodyResponse = objectNodeResponseEntity.getBody();
            final String nodeRef = bodyResponse.get("metadata").get("id").asText();
            alfredDocument = new AlfredDocumentImpl(nodeReferenceBuilder, url, restTemplate, nodeRef);
        } else {
            // FIXME créer une exception typée
            throw new RuntimeException(objectNodeResponseEntity.getStatusCode().toString());
        }
        return alfredDocument;
    }
}
