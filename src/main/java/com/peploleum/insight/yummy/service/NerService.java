package com.peploleum.insight.yummy.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.entities.insight.*;
import com.peploleum.insight.yummy.dto.source.SimpleRawData;
import com.peploleum.insight.yummy.dto.source.ner.NerJsonObjectQuery;
import com.peploleum.insight.yummy.dto.source.ner.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.source.rawtext.RawTextMessage;
import com.peploleum.insight.yummy.dto.source.rss.Item;
import com.peploleum.insight.yummy.dto.source.rss.RssSourceMessage;
import com.peploleum.insight.yummy.dto.source.twitter.TwitterSourceMessage;
import com.peploleum.insight.yummy.service.utils.NerResponseHandler;
import com.peploleum.insight.yummy.service.utils.NerXmlResponseParser;
import com.peploleum.insight.yummy.service.utils.RefGeoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NerService {

    @Value("${urlner}")
    private String urlner;

    @Value("${urlinsight}")
    private String urlinsight;

    @Value("${format}")
    private String format;

    @Value("${ner}")
    private boolean useNer;

    @Value("${graph.enabled}")
    private boolean useGraph;

    @Value("${elasticsearch.enabled}")
    private boolean useElasticSearch;

    @Autowired
    private GraphyService graphyService;

    @Autowired
    private InsightService insightClientService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    private SearchService searchService;

    private final Logger log = LoggerFactory.getLogger(NerService.class);
    private ObjectMapper mapperObj = new ObjectMapper();

    public NerService(SearchService searchService) {
        this.searchService = searchService;
        this.mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public boolean doSend(final Object message) throws Exception {
        if (message instanceof RssSourceMessage) {
            try {
                this.log.info("Processing RSS message");
                final RssSourceMessage rssSourceMessage = (RssSourceMessage) message;
                int cpt = 0;
                if (rssSourceMessage.getChannel() == null || rssSourceMessage.getChannel().getItem() == null) {
                    this.log.warn("Rss message has no readable channel or channel with no content:");
                    return false;
                }
                this.log.info("Items to process: " + rssSourceMessage.getChannel().getItem().size());
                for (Item item : rssSourceMessage.getChannel().getItem()) {
                    final String nerCandidate = (item.getDescription() != null && !item.getDescription().isEmpty()) ? item.getDescription() : item.getTitle();
                    final SimpleRawData simpleRawData = SimpleRawData.fromRssSourceMessage(rssSourceMessage);
                    simpleRawData.setText(nerCandidate);
                    NerJsonObjectResponse nerJsonObjectResponse = null;
                    if (this.useNer) {
                        nerJsonObjectResponse = submitNerRequest(simpleRawData);
                    }
                    createInRemoteServices(simpleRawData, nerJsonObjectResponse);
                    cpt++;
                }
                log.debug("Number of Items processed : " + cpt);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw e;
            }
        } else if (message instanceof TwitterSourceMessage) {
            this.log.info("Processing TWITTER message");
            final TwitterSourceMessage twitterSourceMessage = (TwitterSourceMessage) message;
            final SimpleRawData simpleRawData = SimpleRawData.fromTwitterSourceMessage(twitterSourceMessage);
            NerJsonObjectResponse nerJsonObjectResponse = null;
            if (this.useNer) {
                nerJsonObjectResponse = submitNerRequest(simpleRawData);
            }
            createInRemoteServices(simpleRawData, nerJsonObjectResponse);
        } else if (message instanceof RawTextMessage) {
            this.log.info("Processing RawText message");
            final RawTextMessage rawTextSourceMessage = (RawTextMessage) message;
            final SimpleRawData simpleRawData = SimpleRawData.fromRawTextSourceMessage(rawTextSourceMessage);
            NerJsonObjectResponse nerJsonObjectResponse = null;
            if (this.useNer) {
                nerJsonObjectResponse = submitNerRequest(simpleRawData);
            }
            createInRemoteServices(simpleRawData, nerJsonObjectResponse);
        }
        return true;
    }

    public NerJsonObjectResponse submitNerRequest(final SimpleRawData simpleRawData) throws IOException {
        final NerJsonObjectQuery nerQuery = new NerJsonObjectQuery();
        nerQuery.addsteps("identify_language,tokenize,pos,ner");
        nerQuery.setText(simpleRawData.getText());

        final String dummyPayloadAsString = mapperObj.writeValueAsString(nerQuery);
        log.debug("Payload: " + dummyPayloadAsString);
        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        /*
         * Demande la response de type XML et non JSON du wrapper NER pour obtenir la position des entités dans le texte
         * */
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
        final HttpEntity<NerJsonObjectQuery> entity = new HttpEntity<>(nerQuery, headers);
        try {
            final ResponseEntity<String> tResponseEntity = rt.exchange(this.urlner, HttpMethod.POST, entity, String.class);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(tResponseEntity.getBody())));
            final NerJsonObjectResponse nerObjectResponse = NerXmlResponseParser.getResponseObjectDTO(document);

            nerObjectResponse.setContent(tResponseEntity.getBody());
            log.debug("Received " + tResponseEntity.getBody());
            return nerObjectResponse;
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return null;
    }

    private void createInRemoteServices(SimpleRawData simpleRawData, NerJsonObjectResponse nerObjectResponse) throws Exception {
        final NerResponseHandler responseHandler = new NerResponseHandler(nerObjectResponse, simpleRawData);
        log.info("Sending raw data to Insight");
        final RawData rawData = responseHandler.getRawData();

        // Create RawData
        final String rawDataId = this.insightClientService.create(rawData);
        rawData.setId(rawDataId);
        if (useGraph) {
            try {
                String graphySourceId = this.graphyService.create(rawData);
                rawData.setExternalId(graphySourceId);
                this.insightClientService.update(rawData);
            } catch (RestClientException e) {
                this.log.error("Failed to write in Graphy", e.getMessage());
                throw e;
            } catch (IOException e) {
                this.log.error("Failed to update in Insight", e.getMessage());
                throw e;
            }
        }
        log.info("Sent raw data " + rawDataId + " to Insight  ");

        // Get Extracted entities
        final List<InsightEntity> insightEntities = responseHandler.getInsightEntities();
        log.info("Sending " + insightEntities.size() + " entities to Insight");

        // Extract coordinate if Location is present
        String coordinates = null;
        if (this.useElasticSearch) {
            try {
                final List<String> collect = insightEntities.stream().filter((insightEntity) -> insightEntity instanceof Location).map((insightEntity) -> ((Location) insightEntity).getLocationName()).collect(Collectors.toList());
                for (String locationName : collect) {
                    this.log.info("Found locationName: " + locationName);
                    if (locationName != null) {
                        coordinates = RefGeoUtils.getRefGeoCoordinates(locationName, this.elasticSearchService);
                        if (coordinates != null)
                            break;
                    }
                }
                if (coordinates == null) {
                    this.log.warn("Found no coordinates among " + collect.size() + " locations");
                }
            } catch (Exception e) {
                this.log.error("Error While getting LocationName list", e);
            }
        }
        // Update RawData Location
        if (coordinates != null) {
            this.log.info("Updating raw data coordinates");
            setFieldValue(rawData, "rawDataCoordinates", coordinates);
            this.insightClientService.update(rawData);
        }

        // Check if entities already exists
        List<Object> toCreateEntities = new ArrayList<>();
        List<Object> toUpdateEntities = new ArrayList<>();
        for (Object o : insightEntities) {
            if (o instanceof Biographics) {
                final String biographicsName = this.elasticSearchService.getByNameCriteria(getFieldValue(o, "biographicsName"));

                if (biographicsName == null)
                    toCreateEntities.add(o);
                else {
                    this.log.info("Entity already exists: " + biographicsName);
                    toUpdateEntities.add(o);
                }
            } else {
                this.log.info("Entity does not exist");
                toCreateEntities.add(o);
            }
        }

        // Create new Object
        for (Object o : toCreateEntities) {
            try {
                // Add coordinates
                if (coordinates != null) {
                    if (o instanceof Biographics)
                        setFieldValue(o, "biographicsCoordinates", coordinates);
                    else if (o instanceof Equipment)
                        setFieldValue(o, "equipmentCoordinates", coordinates);
                    else if (o instanceof Event)
                        setFieldValue(o, "eventCoordinates", coordinates);
                    else if (o instanceof Location)
                        setFieldValue(o, "locationCoordinates", coordinates);
                    else if (o instanceof Organisation)
                        setFieldValue(o, "organisationCoordinates", coordinates);
                    else if (o instanceof RawData)
                        setFieldValue(o, "rawDataCoordinates", coordinates);
                }

                // Create in DB
                final String mongoId = this.insightClientService.create(o);
                setFieldValue(o, "id", mongoId);

                // Create in GraphDB
                if (useGraph) {
                    this.log.info("Created Insight Entity: " + o.toString());
                    final String janusId = this.graphyService.create(o);
                    setFieldValue(o, "externalId", janusId);
                    this.log.info("Created Graphy Entity: " + o.toString());
                    this.insightClientService.update(o);
                    this.log.info("Updated Insight Entity: " + o.toString());

                    // BiDirectionnal
                    this.log.info("Creating relation between " + getFieldValue(rawData, "externalId") + " and " + getFieldValue(o, "externalId"));
                    this.graphyService.createRelation(rawData, o);
                    this.graphyService.createRelation(o, rawData);
                }
            } catch (Exception e) {
                this.log.error("Failed to write in Graphy", e);
            }
        }

        if (useGraph) {
            for (Object o : toUpdateEntities) {
                // BiDirectionnal
                this.log.info("Creating relation between " + getFieldValue(rawData, "externalId") + " and " + getFieldValue(o, "externalId"));
                this.graphyService.createRelation(rawData, o);
                this.graphyService.createRelation(o, rawData);
            }
        }

        // Group Entities
        List<Object> allInsightEntities = new ArrayList<>();
        allInsightEntities.addAll(toCreateEntities);
        allInsightEntities.addAll(toUpdateEntities);

        // Create Relation
        if (this.useGraph) {
            for (Object source : allInsightEntities) {
                for (Object target : allInsightEntities) {
                    try {
                        this.log.info("Creating relation between " + getFieldValue(source, "externalId") + " and " + getFieldValue(target, "externalId"));
                        this.graphyService.createRelation(source, target);
                    } catch (Exception e) {
                        this.log.error("Failed to create relation", e.getMessage());
                    }
                }
            }
        }

        // Update le rawData avec les positions des entités dans le texte
        List<InsightEntity> collect = allInsightEntities.stream().filter(e -> e instanceof InsightEntity)
                .map(dto -> (InsightEntity) dto).collect(Collectors.toList());
        List<EntitiesPositionRef> positionRefs = new ArrayList<>();
        for (InsightEntity e : collect) {
            String externalId = getFieldValue(e, "externalId");
            String id = getFieldValue(e, "id");

            String type = "";
            String word = "";
            if (e instanceof Biographics) {
                word = getFieldValue(e, "biographicsFirstname");
                type = Biographics.class.getSimpleName();
            } else if (e instanceof Equipment) {
                word = getFieldValue(e, "equipmentName");
                type = Equipment.class.getSimpleName();
            } else if (e instanceof Event) {
                word = getFieldValue(e, "eventName");
                type = Event.class.getSimpleName();
            } else if (e instanceof Location) {
                word = getFieldValue(e, "locationName");
                type = Location.class.getSimpleName();
            } else if (e instanceof Organisation) {
                word = getFieldValue(e, "organisationName");
                type = Organisation.class.getSimpleName();
            }
            final String entityWord = word;
            final String entityType = type;

            e.getTextPositionInfo().stream().forEach(pos -> positionRefs.add(new EntitiesPositionRef(id, externalId, pos, entityWord, entityType)));
        }
        final ObjectMapper mapper = new ObjectMapper();
        final String positionsToString = mapper.writeValueAsString(positionRefs);
        rawData.setRawDataAnnotations(positionsToString);
        this.insightClientService.update(rawData);

    }

    private static void setFieldValue(Object dto, String fieldName, String externalIdValue) throws IllegalAccessException {
        Field sourceExternalIdField = org.springframework.util.ReflectionUtils.findField(dto.getClass(), fieldName);
        org.springframework.util.ReflectionUtils.makeAccessible(sourceExternalIdField);
        sourceExternalIdField.set(dto, externalIdValue);
    }

    private static String getFieldValue(Object dto, String fieldName) throws IllegalAccessException {
        Field field = org.springframework.util.ReflectionUtils.findField(dto.getClass(), fieldName);
        org.springframework.util.ReflectionUtils.makeAccessible(field);
        return field.get(dto) != null ? field.get(dto).toString() : null;
    }

    class EntitiesPositionRef {
        private String idMongo;
        private String idJanus;
        private Integer position;
        private String entityWord;
        private String entityType;

        public EntitiesPositionRef(String idMongo, String idJanus, Integer position, String entityWord, String entityType) {
            this.idMongo = idMongo;
            this.idJanus = idJanus;
            this.position = position;
            this.entityWord = entityWord;
            this.entityType = entityType;
        }

        public String getIdMongo() {
            return idMongo;
        }

        public void setIdMongo(String idMongo) {
            this.idMongo = idMongo;
        }

        public String getIdJanus() {
            return idJanus;
        }

        public void setIdJanus(String idJanus) {
            this.idJanus = idJanus;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        public String getEntityWord() {
            return entityWord;
        }

        public void setEntityWord(String entityWord) {
            this.entityWord = entityWord;
        }

        public String getEntityType() {
            return entityType;
        }

        public void setEntityType(String entityType) {
            this.entityType = entityType;
        }
    }
}
