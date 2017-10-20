package be.liege.ged.apix.vdl.impl;

import be.liege.ged.apix.vdl.api.AlfredDocument;
import be.liege.ged.apix.vdl.api.AlfredPath;
import be.liege.ged.apix.vdl.api.AlfredService;
import be.liege.ged.apix.vdl.api.PathNotFoundException;
import be.liege.ged.apix.vdl.util.NodeReference;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class AlfredServiceImpl implements AlfredService {

    private static final Logger logger = LoggerFactory.getLogger(AlfredServiceImpl.class);

    private final AlfredFactory alfredFactory;
    private final String url;
    private final RestTemplate restTemplate;

    AlfredServiceImpl(@NotNull AlfredFactory alfredFactory,
                      @NotNull final String url,
                      @NotNull final String username,
                      @Null final String password) {
        this.alfredFactory = alfredFactory;
        this.url = url;
        restTemplate = new RestTemplateBuilder().build();
        restTemplate.getInterceptors().add(
                new BasicAuthorizationInterceptor(username, password != null ? password : ""));
        logger.debug("Creation of AlfredServiceImpl for URL {}", url);
    }

    AlfredServiceImpl(@NotNull AlfredFactory alfredFactory, @NotNull final String url, @NotNull final String username) {
        this(alfredFactory, url, username, null);
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
                    -> new AlfredDocumentBuilderWithTNameAndNameAndTypeImpl(alfredFactory, url, restTemplate, nodeRef, name, type);
            logger.debug("Found: {}", nodeRef);
        } else {
            throw new PathNotFoundException(path, objectNodeResponseEntity.getStatusCode());
        }
        return alfredPath;
    }

    @Override
    public AlfredDocument documentByNodeReference(NodeReference nodeReference) {
        return new AlfredDocumentImpl(alfredFactory, url, restTemplate, nodeReference);
    }

    @Override
    public void deleteByNodeReference(NodeReference nodeReference) {
        final String restUrl = String.format("%s%s/%s/%s/%s",
                url, AlfredConstants.NODES, nodeReference.getSpace(), nodeReference.getStore(), nodeReference.getGuid());
        logger.debug("Suppression du noeud {}", restUrl);
        restTemplate.delete(restUrl);
    }

}
