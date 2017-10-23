package be.liege.ged.apix.vdl.api;

import be.liege.ged.apix.vdl.util.NodeReference;

public interface AlfredService {

    AlfredPath pathSearchByName(final String name) throws PathNotFoundException;

    // AlfredDocument documentSearchByPathAndName(final String path, final String name);

    AlfredDocument documentByNodeReference(final NodeReference nodeReference);

    void deleteByNodeReference(NodeReference nodeReference);

    String getUrl();
}
