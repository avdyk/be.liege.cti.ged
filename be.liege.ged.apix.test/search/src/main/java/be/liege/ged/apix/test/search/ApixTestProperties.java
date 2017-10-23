package be.liege.ged.apix.test.search;

public interface ApixTestProperties {

    String ALFRESCO_URL = "${alfred.url:http://alfresco-test:8080/alfresco/s}";
    String ALFRESCO_USERNAME = "${alfred.username:admin}";
    String ALFRESCO_PASSWORD = "${alfred.password:admin}";
}
