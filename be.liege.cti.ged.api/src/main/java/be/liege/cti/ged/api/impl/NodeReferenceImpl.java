package be.liege.cti.ged.api.impl;

import be.liege.cti.ged.api.NodeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeReferenceImpl implements NodeReference {

    private static final Logger logger = LoggerFactory.getLogger(NodeReferenceImpl.class);
    private final String nodeReference;
    private final String space;
    private final String store;
    private final String guid;

    NodeReferenceImpl(final String nodeReference) {
        this.nodeReference = nodeReference;
        final String[] split1 = nodeReference.split("://");
        this.space = split1[0];
        String[] split2 = split1[1].split("/");
        this.store = split2[0];
        this.guid = split2[1];
        logger.debug("Node reference: {}; space: {}; store: {}; guid: {}",
                nodeReference, space, store, guid);
    }

    @Override
    public String getNodeReference() {
        return nodeReference;
    }

    @Override
    public String getSpace() {
        return space;
    }

    @Override
    public String getStore() {
        return store;
    }

    @Override
    public String getGuid() {
        return guid;
    }
}
