package be.liege.ged.apix.vdl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeReference {

    private static final Logger logger = LoggerFactory.getLogger(NodeReference.class);
    private final String nodeReference;
    private final String space;
    private final String store;
    private final String guid;

    public NodeReference(final String nodeReference) {
        this.nodeReference = nodeReference;
        final String[] split1 = nodeReference.split("://");
        this.space = split1[0];
        String[] split2 = split1[1].split("/");
        this.store = split2[0];
        this.guid = split2[1];
        logger.debug("Node reference: {}; space: {}; store: {}; guid: {}",
                nodeReference, space, store, guid);
    }

    public String getNodeReference() {
        return nodeReference;
    }

    public String getSpace() {
        return space;
    }

    public String getStore() {
        return store;
    }

    public String getGuid() {
        return guid;
    }
}
