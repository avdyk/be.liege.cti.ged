package be.liege.ged.apix.vdl.impl;

import be.liege.ged.apix.vdl.api.AlfredDocument;
import be.liege.ged.apix.vdl.util.NodeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class AlfredDocumentImpl implements AlfredDocument {

    private static final Logger logger = LoggerFactory.getLogger(AlfredDocumentImpl.class);

    private final AlfredFactory alfredFactory;
    private final String url;
    private final RestTemplate restTemplate;
    private final NodeReference nodeReference;

    AlfredDocumentImpl(@NotNull final AlfredFactory alfredFactory,
                       @NotNull final String url,
                       @NotNull RestTemplate restTemplate,
                       @NotNull String nodeReferenceString) {
        this.alfredFactory = alfredFactory;
        this.url = url;
        this.restTemplate = restTemplate;
        this.nodeReference = alfredFactory.nodeReference(nodeReferenceString);
    }

    AlfredDocumentImpl(@NotNull final AlfredFactory alfredFactory,
                       @NotNull final String url,
                       @NotNull RestTemplate restTemplate,
                       @NotNull NodeReference nodeReference) {
        this.alfredFactory = alfredFactory;
        this.url = url;
        this.restTemplate = restTemplate;
        this.nodeReference = nodeReference;
    }

    @Override
    public AlfredDocument content(final Path location) {
        final String restUrl = String.format("%s%s/%s/%s/%s/content",
                url, AlfredConstants.NODES, nodeReference.getSpace(), nodeReference.getStore(), nodeReference.getGuid());
        logger.debug("Ajout de contenu à l'url {}", restUrl);
        final byte[] data;
        try {
            data = Files.readAllBytes(location);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        HttpResponse<JsonNode> jsonresponse = Unirest.put(hosturl+url)
//                .basicAuth("admin", "admin")
//                .header("accept", "application/json")
//                .field("file", data, null)
//                // .body(data) // Same problem
//                .asJson();
        restTemplate.put(restUrl, "file", data);
        return this;
    }

    @Override
    public AlfredDocument metadata(Properties properties) {

        /*
            public String setMetadata(String nodeRef) throws IOException, UnirestException, ParseException {
        String[] splitRef = Utils.splitNodeRef(nodeRef);
        JSONParser parser = new JSONParser();
        String url = "apix/v1/nodes/"+splitRef[0]+"/"+splitRef[1]+"/"+splitRef[2]+"/metadata";
        String JSONString = "{\n" +
                "  \"aspectsToAdd\": [\"{http://www.alfresco.org/model/system/1.0}temporary\"],\n" +
                "  \"propertiesToSet\": {\"{http://www.alfresco.org/model/content/1.0}title\":[\"My new title\"]}\n" +
                "}";
        JSONObject body = (JSONObject) parser.parse(JSONString);
        return execute(body, url, "POST");

         */


        return null;
    }

    @Override
    public NodeReference getNodeReference() {
        return nodeReference;
    }

    @Override
    public String getUrl() {
        return url;
    }

}
