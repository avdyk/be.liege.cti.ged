package be.liege.cti.ged.api.impl.search;

import be.liege.cti.ged.api.search.AlfredCompositeQueryBuilder;
import be.liege.cti.ged.api.search.AlfredQuery;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AlfredCompositeQueryBuilderImpl implements AlfredCompositeQueryBuilder {

    public static final String OR = "or";
    public static final String AND = "and";

    static class AlfredQueryImpl implements AlfredQuery {

        private final ObjectNode body;

        public AlfredQueryImpl(final ObjectNode body) {
            this.body = body;
        }

        public ObjectNode getBody() {
            return this.body;
        }
    }

    @Override
    public AlfredQuery and(AlfredQuery... queries) {
        return this.collect(AND, queries);
    }

    @Override
    public AlfredQuery or(AlfredQuery... queries) {
        return this.collect(OR, queries);
    }

    private AlfredQuery collect(final String op, final AlfredQuery[] queries) {
        ObjectNode operator = JsonNodeFactory.instance.objectNode();
        operator.putArray(op)
                .addAll(Arrays.stream(queries)
                        .filter(AlfredQueryImpl.class::isInstance)
                        .map(AlfredQueryImpl.class::cast)
                        .map(AlfredQueryImpl::getBody)
                        .collect(Collectors.toList()));
        return new AlfredQueryImpl(operator);
    }

    @Override
    public AlfredQuery path(String searchPath) {
        return new AlfredQueryImpl(JsonNodeFactory.instance.objectNode()
                .put("path", searchPath));
    }

}
