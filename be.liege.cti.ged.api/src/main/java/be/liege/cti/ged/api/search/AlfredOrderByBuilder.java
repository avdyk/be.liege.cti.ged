package be.liege.cti.ged.api.search;

public interface AlfredOrderByBuilder {

    AlfredOrderByBuilder orderBy(final String property, final AlfredOrderBy.Ordering ordering);

    AlfredSearchBuilder build();
}
