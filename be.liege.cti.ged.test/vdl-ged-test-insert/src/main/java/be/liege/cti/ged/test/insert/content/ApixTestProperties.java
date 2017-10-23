package be.liege.cti.ged.test.insert.content;

public interface ApixTestProperties {

    String ALFRESCO_URL = "${alfred.url:http://alfresco-test:8080/alfresco/s}";
    String ALFRESCO_USERNAME = "${alfred.username:admin}";
    String ALFRESCO_PASSWORD = "${alfred.password:admin}";
    String NEW_DOCUMENT_PATH = "${alfred.new-document.path:/app:company_home/cm:VDL}";
    String NEW_DOCUMENT_NAME = "${alfred.new-document.name:charly.jpeg}";
    String NEW_DOCUMENT_LOCATION = "${alfred.new-document.location:src/main/resources/charly.jpeg}";
}
