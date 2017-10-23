package be.liege.cti.ged.api.search;

import be.liege.cti.ged.api.NodeReference;

import java.util.List;
import java.util.stream.Stream;

public interface AlfredSearch {

    Stream<NodeReference> nodeReferences();

    int getSkip();

    int getLimit();

    boolean isWithFacets();

    List<AlfredOrderBy> getOrderByList();

    AlfredQuery getQuery();

}
