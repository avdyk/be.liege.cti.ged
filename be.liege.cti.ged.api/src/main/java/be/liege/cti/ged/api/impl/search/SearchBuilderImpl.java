package be.liege.cti.ged.api.impl.search;

import be.liege.cti.ged.api.NodeReferenceBuilder;
import be.liege.cti.ged.api.search.AlfredCompositeQueryBuilder;
import be.liege.cti.ged.api.search.AlfredOrderBy;
import be.liege.cti.ged.api.search.AlfredOrderByBuilder;
import be.liege.cti.ged.api.search.AlfredQuery;
import be.liege.cti.ged.api.search.AlfredSearch;
import be.liege.cti.ged.api.search.AlfredSearchBuilder;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class SearchBuilderImpl implements AlfredSearchBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SearchBuilderImpl.class);

    private final NodeReferenceBuilder nodeReferenceBuilder;
    private final String url;
    private final RestTemplate restTemplate;
    private int skip;
    private int limit;
    private boolean withFacets;
    private List<AlfredOrderBy> orderByList;
    private AlfredCompositeQueryBuilderImpl.AlfredQueryImpl query;

    public SearchBuilderImpl(final NodeReferenceBuilder nodeReferenceBuilder,
                             final String url,
                             final RestTemplate restTemplate) {
        this.nodeReferenceBuilder = nodeReferenceBuilder;
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public AlfredSearchBuilder skip(int skip) {
        this.skip = skip;
        return this;
    }

    @Override
    public AlfredSearchBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public AlfredSearchBuilder withFacets() {
        this.withFacets = true;
        return this;
    }

    @Override
    public AlfredSearchBuilder withoutFacets() {
        this.withFacets = false;
        return this;
    }

    @Override
    public AlfredOrderByBuilder orderBy() {
        return new AlfredOrderByBuilder() {
            final List<AlfredOrderBy> orderByList = new ArrayList<>();
            @Override
            public AlfredOrderByBuilder orderBy(final String property, final AlfredOrderBy.Ordering ordering) {
                orderByList.add(new AlfredOrderBy() {
                    @Override
                    public String getProperty() {
                        return property;
                    }

                    @Override
                    public Ordering getOrdering() {
                        return ordering;
                    }
                });
                return this;
            }

            @Override
            public AlfredSearchBuilder build() {
                SearchBuilderImpl.this.orderByList = this.orderByList;
                return SearchBuilderImpl.this;
            }
        };
    }

    @Override
    public AlfredCompositeQueryBuilder query() {
        return new AlfredCompositeQueryBuilderImpl();
    }

    @Override
    public AlfredSearchBuilder query(AlfredQuery query) {
        if (query instanceof AlfredCompositeQueryBuilderImpl.AlfredQueryImpl) {
            ObjectNode q = JsonNodeFactory.instance.objectNode();
            q.putPOJO("query", ((AlfredCompositeQueryBuilderImpl.AlfredQueryImpl) query).getBody());
            this.query = new AlfredCompositeQueryBuilderImpl.AlfredQueryImpl(q);
        } else {
            throw new IllegalArgumentException("Bad query (should be a AlfredCompositeQueryBuilderImpl.AlfredQueryImpl)");
        }
        return this;
    }

    @Override
    public AlfredSearch build() {
        return new AlfredSearchImpl(nodeReferenceBuilder, url, restTemplate, skip, limit, withFacets, orderByList, query);
    }
}
