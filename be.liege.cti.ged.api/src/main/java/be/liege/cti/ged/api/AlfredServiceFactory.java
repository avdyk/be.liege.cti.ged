package be.liege.cti.ged.api;

public interface AlfredServiceFactory {

    AlfredServiceBuilder createAlfredServiceBuilder();

    NodeReferenceBuilder nodeReferenceBuilder();
}
