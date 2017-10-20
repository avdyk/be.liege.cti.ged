package be.liege.ged.apix.vdl.impl;

import be.liege.ged.apix.vdl.api.AlfredBuilder;
import be.liege.ged.apix.vdl.util.NodeReference;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class AlfredFactory {

    public AlfredBuilder alfredBuilder() {
        return url -> login -> password -> new AlfredServiceImpl(this, url, login, password);
    }

    public NodeReference nodeReference(@NotNull final String nodeReference) {
        return new NodeReference(nodeReference);
    }

}
