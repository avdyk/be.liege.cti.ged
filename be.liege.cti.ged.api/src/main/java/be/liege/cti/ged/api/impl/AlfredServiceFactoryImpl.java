package be.liege.cti.ged.api.impl;

import be.liege.cti.ged.api.AlfredServiceBuilder;
import be.liege.cti.ged.api.AlfredServiceFactory;
import be.liege.cti.ged.api.NodeReferenceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

@Component
public class AlfredServiceFactoryImpl implements AlfredServiceFactory {

    private final RestTemplateBuilder restTemplateBuilder;

    @Autowired
    public AlfredServiceFactoryImpl(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public AlfredServiceBuilder createAlfredServiceBuilder() {
        return url -> login -> password -> new AlfredServiceImpl(restTemplateBuilder,
                nodeRefString -> new NodeReferenceImpl(nodeRefString),
                url, login, password);
    }

    @Override
    public NodeReferenceBuilder nodeReferenceBuilder() {
        return nodeRefString -> new NodeReferenceImpl(nodeRefString);
    }

}
