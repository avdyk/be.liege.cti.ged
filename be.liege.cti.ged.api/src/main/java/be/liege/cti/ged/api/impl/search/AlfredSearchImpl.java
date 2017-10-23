package be.liege.cti.ged.api.impl.search;

import be.liege.cti.ged.api.AlfredConstants;
import be.liege.cti.ged.api.AlfredPath;
import be.liege.cti.ged.api.NodeReference;
import be.liege.cti.ged.api.NodeReferenceBuilder;
import be.liege.cti.ged.api.search.AlfredOrderBy;
import be.liege.cti.ged.api.search.AlfredSearch;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AlfredSearchImpl implements AlfredSearch {

    private static final Logger logger = LoggerFactory.getLogger(AlfredSearchImpl.class);

    private final NodeReferenceBuilder nodeReferenceBuilder;
    private final String url;
    private final RestTemplate restTemplate;
    private final int skip;
    private final int limit;
    private final boolean withFacets;
    private final List<AlfredOrderBy> orderByList;
    private final AlfredCompositeQueryBuilderImpl.AlfredQueryImpl query;

    public AlfredSearchImpl(final NodeReferenceBuilder nodeReferenceBuilder,
                            final String url,
                            final RestTemplate restTemplate,
                            final int skip,
                            final int limit,
                            final boolean withFacets,
                            final List<AlfredOrderBy> orderByList,
                            final AlfredCompositeQueryBuilderImpl.AlfredQueryImpl query) {
        this.nodeReferenceBuilder = nodeReferenceBuilder;
        this.url = url;
        this.restTemplate = restTemplate;
        this.skip = skip;
        this.limit = limit;
        this.withFacets = withFacets;
        this.orderByList = orderByList;
        this.query = query;
    }

    @Override
    public Stream<NodeReference> nodeReferences() {
        final String searchUrl = url + AlfredConstants.SEARCH;
        logger.debug("Search url: {}", searchUrl);
        logger.debug("Recherche: {}", query != null ? query.getBody().toString() : "NULL");
        final ResponseEntity<ObjectNode> objectNodeResponseEntity =
                restTemplate.postForEntity(searchUrl, query.getBody(), ObjectNode.class);
        final AlfredPath alfredPath;
        if (objectNodeResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            final ObjectNode bodyResponse = objectNodeResponseEntity.getBody();
            final JsonNode nodeRefs = bodyResponse.get("noderefs");
            final Iterator<JsonNode> iterator = nodeRefs.iterator();
            Stream<JsonNode> jsonStream = StreamSupport.stream(Spliterators.spliterator(iterator, nodeRefs.size(), Spliterator.ORDERED)
                    , true);
            return jsonStream
                    .map(JsonNode::asText)
                    .map(nodeReferenceBuilder::nodeReference);
        } else {
            logger.warn("Problème pour retrouver les informations: {}", objectNodeResponseEntity.getStatusCode());
        }
        return Stream.empty();
    }

    @Override
    public int getSkip() {
        return skip;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public boolean isWithFacets() {
        return withFacets;
    }

    @Override
    public List<AlfredOrderBy> getOrderByList() {
        return orderByList;
    }

    @Override
    public AlfredCompositeQueryBuilderImpl.AlfredQueryImpl getQuery() {
        return query;
    }
}
