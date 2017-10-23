package be.liege.ged.apix.vdl.api;

import java.util.Properties;

public interface AlfredDocumentBuilderWithNameAndType {

    AlfredDocumentBuilderWithNameAndType properties(final Properties properties);

    AlfredDocument create();

}
