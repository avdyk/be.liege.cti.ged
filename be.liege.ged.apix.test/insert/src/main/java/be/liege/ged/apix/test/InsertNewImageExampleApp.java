package be.liege.ged.apix.test;

import be.liege.ged.apix.vdl.api.AlfredConfiguration;
import be.liege.ged.apix.vdl.util.NodeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Null;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@SpringBootApplication(scanBasePackages = "be.liege.ged.apix")
@Import(AlfredConfiguration.class)
public class InsertNewImageExampleApp {

    private static final Logger logger = LoggerFactory.getLogger(InsertNewImageExampleApp.class);
    public static final String TYPE = "{http://www.alfresco.org/model/content/1.0}content";

    private final String alfredUrl;
    private final String alfredUsername;
    private final String alfredPassword;
    private final String newDocumentPath;
    private final String newDocumentName;
    private final String newDocumentLocation;
    private final AlfredConfiguration alfredFactory;
    private final Optional<NodeReference> nodeReferenceAddContent;

    public InsertNewImageExampleApp(@Value(ApixTestProperties.ALFRESCO_URL) final String alfredUrl,
                                    @Value(ApixTestProperties.ALFRESCO_USERNAME) final String alfredUsername,
                                    @Value(ApixTestProperties.ALFRESCO_PASSWORD) final String alfredPassword,
                                    @Value(ApixTestProperties.NEW_DOCUMENT_PATH) final String newDocumentPath,
                                    @Value(ApixTestProperties.NEW_DOCUMENT_NAME) final String newDocumentName,
                                    @Value(ApixTestProperties.NEW_DOCUMENT_LOCATION) final String newDocumentLocation,
                                    @Null @Value("${node.content}") final String nodeContent,
                                    final AlfredConfiguration alfredFactory) {
        this.alfredUrl = alfredUrl;
        this.alfredUsername = alfredUsername;
        this.alfredPassword = alfredPassword;
        this.alfredFactory = alfredFactory;
        this.newDocumentPath = newDocumentPath;
        this.newDocumentName = newDocumentName;
        this.newDocumentLocation = newDocumentLocation;
        if (StringUtils.isEmpty(nodeContent)) {
            nodeReferenceAddContent = Optional.empty();
        } else {
            nodeReferenceAddContent = Optional.of(new NodeReference(nodeContent));
        }
        logger.debug("alfred.url: {}", alfredUrl);
    }


    public static void main(String args[]) {
        SpringApplication.run(InsertNewImageExampleApp.class);
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        final RestTemplate rt = builder.build();
        // rt.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());
        return rt;
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            restTemplate.getInterceptors().add(
                    new BasicAuthorizationInterceptor(alfredUsername, alfredPassword));
            logger.info("Ajout d'un nouveau document: {} / {} [{}]", newDocumentPath, newDocumentName, newDocumentLocation);
            final Path location = Paths.get(newDocumentLocation);
            logger.info("Le fichier {} {}", location, Files.exists(location) ? "existe" : "n'existe pas");
            final NodeReference nodeReference;
            if (nodeReferenceAddContent.isPresent()) {
                nodeReference = nodeReferenceAddContent.map(nodeRef -> alfredFactory.alfredBuilder()
                        .url(alfredUrl)
                        .login(alfredUsername)
                        .password(alfredPassword)
                        .documentByNodeReference(nodeRef)
                        .content(location)
                        .getNodeReference())
                        .orElseThrow(() -> new RuntimeException("Problème dans la sauvegarde du contenu"));

            } else {
                nodeReference = alfredFactory.alfredBuilder()
                        .url(alfredUrl)
                        .login(alfredUsername)
                        .password(alfredPassword)
                        .pathSearchByName(newDocumentPath)
                        .createNewDocument()
                        .name(newDocumentName)
                        .type(TYPE)
                        .create()
                        .content(location)
                        .getNodeReference();
            }
            logger.info("Le document {} a été poussé dans Alfresco. Il s'agit du noeud {}",
                    newDocumentName,
                    nodeReference.getNodeReference());
        };
    }

}
