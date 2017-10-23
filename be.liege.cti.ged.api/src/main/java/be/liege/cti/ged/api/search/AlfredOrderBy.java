package be.liege.cti.ged.api.search;

public interface AlfredOrderBy {

    enum Ordering {
        ASCENDING,
        DESCENDING
    }


    String getProperty();

    Ordering getOrdering();

}
