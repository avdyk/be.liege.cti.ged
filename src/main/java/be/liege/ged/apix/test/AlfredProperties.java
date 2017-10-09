package be.liege.ged.apix.test;

public interface AlfredProperties {

    String ALFRESCO_URL = "${alfred.url}";
    String ALFRESCO_USERNAME = "${alfred.username:admin}";
    String ALFRESCO_PASSWORD = "${alfred.password:admin}";
}
