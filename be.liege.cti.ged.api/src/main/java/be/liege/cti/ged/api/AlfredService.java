package be.liege.cti.ged.api;

import be.liege.cti.ged.api.search.AlfredSearchBuilder;

public interface AlfredService {

    AlfredSearchBuilder searchBuilder();

    AlfredPath pathSearchByName(final String name) throws PathNotFoundException;

    // AlfredDocument documentSearchByPathAndName(final String path, final String name);

    AlfredDocument documentByNodeReference(final NodeReference nodeReference);

    void deleteByNodeReference(NodeReference nodeReference);

    String getUrl();
}
