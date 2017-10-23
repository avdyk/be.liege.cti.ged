package be.liege.cti.ged.test.insert.content;

import be.liege.cti.ged.api.AlfredConfiguration;
import be.liege.cti.ged.api.AlfredDocument;
import be.liege.cti.ged.api.AlfredService;
import be.liege.cti.ged.api.AlfredServiceFactory;
import be.liege.cti.ged.api.NodeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;

@SpringBootApplication
@Import(AlfredConfiguration.class)
public class InsertNewImageExampleApp {

    private static final Logger logger = LoggerFactory.getLogger(InsertNewImageExampleApp.class);

    private final String alfredUrl;
    private final String alfredUsername;
    private final String alfredPassword;
    private final String newDocumentPath;
    private final String newDocumentName;
    private final String newDocumentLocation;
    private final AlfredServiceFactory alfredFactory;
    private final Optional<NodeReference> nodeReferenceAddContent;

    @Autowired
    public InsertNewImageExampleApp(final AlfredServiceFactory alfredFactory,
                                    @Value(ApixTestProperties.ALFRESCO_URL) final String alfredUrl,
                                    @Value(ApixTestProperties.ALFRESCO_USERNAME) final String alfredUsername,
                                    @Value(ApixTestProperties.ALFRESCO_PASSWORD) final String alfredPassword,
                                    @Value(ApixTestProperties.NEW_DOCUMENT_PATH) final String newDocumentPath,
                                    @Value(ApixTestProperties.NEW_DOCUMENT_NAME) final String newDocumentName,
                                    @Value(ApixTestProperties.NEW_DOCUMENT_LOCATION) final String newDocumentLocation,
                                    @Value("${node.content}") final String nodeContent) {
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
            nodeReferenceAddContent = Optional.of(alfredFactory.nodeReferenceBuilder().nodeReference(nodeContent));
        }
        logger.debug("alfred.url: {}", alfredUrl);
    }


    public static void main(String args[]) {
        SpringApplication.run(InsertNewImageExampleApp.class);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            logger.info("Ajout d'un nouveau document: {} / {} [{}]", newDocumentPath, newDocumentName, newDocumentLocation);
            final Path location = Paths.get(newDocumentLocation);
            logger.debug("Le fichier {} {}", location, Files.exists(location) ? "existe" : "n'existe pas");
            final AlfredService alfredService = alfredFactory.createAlfredServiceBuilder()
                    .url(alfredUrl)
                    .username(alfredUsername)
                    .password(alfredPassword);

            final Supplier<NodeReference> createDocumentFromProperties = () -> createDocument(alfredService);

            final NodeReference nodeReference = alfredService
                    .documentByNodeReference(nodeReferenceAddContent.orElseGet(createDocumentFromProperties))
                    .content(location)
                    .getNodeReference();
            logger.info("Le document {} a été poussé dans Alfresco. Il s'agit du noeud {}",
                    newDocumentName,
                    nodeReference.getNodeReference());
        };
    }

    private NodeReference createDocument(final AlfredService alfredService) {
        return alfredService
                .pathSearchByName(newDocumentPath)
                .createNewDocument()
                .name(newDocumentName)
                .type(AlfredDocument.TYPE_CONTENT)
                .create().getNodeReference();
    }


}
