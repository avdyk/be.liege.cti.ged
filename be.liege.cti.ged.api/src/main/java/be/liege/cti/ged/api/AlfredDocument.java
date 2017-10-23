package be.liege.cti.ged.api;

import java.nio.file.Path;
import java.util.Properties;

public interface AlfredDocument {

    String TYPE_CONTENT = "{http://www.alfresco.org/model/content/1.0}content";

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
