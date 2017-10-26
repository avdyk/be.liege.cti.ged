package be.liege.cti.ged.api.search;

public interface AlfredSearchBuilder {

    AlfredSearchBuilder skip(final int skip);

    AlfredSearchBuilder limit(final int limit);

    AlfredSearchBuilder withFacets();

    /**
     * The default.
     *
     * @return the search builder.
     */
    AlfredSearchBuilder withoutFacets();

    AlfredOrderByBuilder orderBy();

    AlfredRangeBuilder range();

    AlfredCompositeQueryBuilder query();

    AlfredSearchBuilder query(final AlfredQuery query);

    AlfredSearch build();

}
