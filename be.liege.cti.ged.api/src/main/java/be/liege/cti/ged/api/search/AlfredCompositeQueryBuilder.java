package be.liege.cti.ged.api.search;

public interface AlfredCompositeQueryBuilder extends AlfredQueryBuilder {

    AlfredQuery and(final AlfredQuery...queries);

    AlfredQuery or(final AlfredQuery...queries);

}
