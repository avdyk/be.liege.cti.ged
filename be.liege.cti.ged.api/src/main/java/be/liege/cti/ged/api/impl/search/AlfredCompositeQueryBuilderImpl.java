package be.liege.cti.ged.api.impl.search;

import be.liege.cti.ged.api.NodeReference;
import be.liege.cti.ged.api.search.AlfredCompositeQueryBuilder;
import be.liege.cti.ged.api.search.AlfredQuery;
import be.liege.cti.ged.api.search.Range;
import be.liege.cti.ged.api.search.RangeDate;
import be.liege.cti.ged.api.search.RangeInt;
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

        @Override
        public AlfredQuery not() {
            final ObjectNode not = AlfredCompositeQueryBuilderImpl.newNode();
            not.putPOJO("not", this.body);
            return new AlfredQueryImpl(not);
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
        final ObjectNode operator = AlfredCompositeQueryBuilderImpl.newNode();
        operator.putArray(op)
                .addAll(Arrays.stream(queries)
                        .filter(AlfredQueryImpl.class::isInstance)
                        .map(AlfredQueryImpl.class::cast)
                        .map(AlfredQueryImpl::getBody)
                        .collect(Collectors.toList()));
        return new AlfredQueryImpl(operator);
    }

    @Override
    public AlfredQuery all(String search) {
        return new AlfredQueryImpl(AlfredCompositeQueryBuilderImpl.newNode()
                .put("all", search));
    }

    @Override
    public AlfredQuery aspect(String aspect) {
        return new AlfredQueryImpl(AlfredCompositeQueryBuilderImpl.newNode()
                .put("aspect", aspect));
    }

    @Override
    public AlfredQuery category(String category) {
        return new AlfredQueryImpl(AlfredCompositeQueryBuilderImpl.newNode()
                .put("category", category));
    }

    @Override
    public AlfredQuery nodeReference(NodeReference nodeReference) {
        return new AlfredQueryImpl(AlfredCompositeQueryBuilderImpl.newNode()
                .put("noderef", nodeReference.getNodeReference()));
    }

    @Override
    public AlfredQuery parent(NodeReference child) {
        return new AlfredQueryImpl(AlfredCompositeQueryBuilderImpl.newNode()
                .put("parent", child.getNodeReference()));
    }

    @Override
    public AlfredQuery path(String searchPath) {
        return new AlfredQueryImpl(AlfredCompositeQueryBuilderImpl.newNode()
                .put("path", searchPath));
    }

    @Override
    public AlfredQuery property(String name, String value, boolean exact) {
        final ObjectNode propertyNode = AlfredCompositeQueryBuilderImpl.newNode();
        final ObjectNode props = AlfredCompositeQueryBuilderImpl.newNode();
        props.put("name", name);
        props.put("value", value);
        props.put("exact", exact);
        propertyNode.putPOJO("property", props);
        return new AlfredQueryImpl(propertyNode);
    }

    @Override
    public AlfredQuery property(String name, Range range) {
        final ObjectNode propertyNode = AlfredCompositeQueryBuilderImpl.newNode();
        final ObjectNode props = AlfredCompositeQueryBuilderImpl.newNode();
        final ObjectNode r = AlfredCompositeQueryBuilderImpl.newNode();
        props.put("name", name);
        if (range instanceof RangeDate) {
            r.put("start", ((RangeDate) range).getStart().toString());
            r.put("end", ((RangeDate) range).getEnd().toString());
        } else if (range instanceof RangeInt) {
            r.put("start", ((RangeInt) range).getStart());
            r.put("end", ((RangeInt) range).getEnd());
        } else {
            throw new IllegalArgumentException("Range inconnu!");
        }
        props.putPOJO("range", r);
        propertyNode.putPOJO("property", props);
        return new AlfredQueryImpl(propertyNode);
    }

    @Override
    public AlfredQuery text(String text) {
        return new AlfredQueryImpl(AlfredCompositeQueryBuilderImpl.newNode()
                .put("text", text));
    }

    @Override
    public AlfredQuery type(String type) {
        return type(type, false);
    }

    @Override
    public AlfredQuery type(String type, boolean exact) {
        final ObjectNode typeNode = AlfredCompositeQueryBuilderImpl.newNode();
        final ObjectNode values = AlfredCompositeQueryBuilderImpl.newNode()
                .put("value", type)
                .put("exact", exact);
        typeNode.putPOJO("type", values);
        return new AlfredQueryImpl(typeNode);
    }

    private static ObjectNode newNode() {
        return JsonNodeFactory.instance.objectNode();
    }

}
