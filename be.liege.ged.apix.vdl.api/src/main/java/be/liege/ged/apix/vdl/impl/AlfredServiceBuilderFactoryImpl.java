package be.liege.ged.apix.vdl.impl;

import be.liege.ged.apix.vdl.api.AlfredServiceBuilder;
import be.liege.ged.apix.vdl.api.AlfredServiceBuilderFactory;
import be.liege.ged.apix.vdl.api.NodeReferenceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

@Component
public class AlfredServiceBuilderFactoryImpl implements AlfredServiceBuilderFactory {

    private final RestTemplateBuilder restTemplateBuilder;
    private final NodeReferenceBuilder nodeReferenceBuilder;

    @Autowired
    public AlfredServiceBuilderFactoryImpl(final RestTemplateBuilder restTemplateBuilder,
                                           final NodeReferenceBuilder nodeReferenceBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.nodeReferenceBuilder = nodeReferenceBuilder;
    }

    @Override
    public AlfredServiceBuilder createAlfredServiceBuilder() {
        return url -> login -> password -> new AlfredServiceImpl(restTemplateBuilder, nodeReferenceBuilder, url, login, password);
    }
}
