package be.liege.cti.ged.api;

import java.util.Properties;

public interface AlfredDocumentBuilderWithNameAndType {

    AlfredDocumentBuilderWithNameAndType properties(final Properties properties);

    AlfredDocument create();

}
