package be.liege.ged.apix.vdl.impl;

import be.liege.ged.apix.vdl.api.NodeReferenceBuilder;
import be.liege.ged.apix.vdl.util.NodeReference;
import org.springframework.stereotype.Component;

@Component
public class NodeReferenceBuilderImpl implements NodeReferenceBuilder {
    @Override
    public NodeReference nodeReference(final String nodeReference) {
        return new NodeReference(nodeReference);
    }
}
