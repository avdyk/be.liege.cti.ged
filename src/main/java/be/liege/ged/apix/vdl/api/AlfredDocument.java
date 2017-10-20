package be.liege.ged.apix.vdl.api;

import be.liege.ged.apix.vdl.util.NodeReference;

import java.nio.file.Path;
import java.util.Properties;

public interface AlfredDocument {

    /**
     * Upload the content of the document.
     *
     * @param location the location of the document to upload.
     * @return the node reference.
     */
    AlfredDocument content(final Path location);

    AlfredDocument metadata(final Properties properties);

    NodeReference getNodeReference();

    String getUrl();

}
