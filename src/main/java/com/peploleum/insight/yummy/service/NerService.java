package com.peploleum.insight.yummy.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.entities.insight.*;
import com.peploleum.insight.yummy.dto.source.SimpleRawData;
import com.peploleum.insight.yummy.dto.source.ner.NerJsonObjectQuery;
import com.peploleum.insight.yummy.dto.source.ner.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.source.rss.Item;
import com.peploleum.insight.yummy.dto.source.rss.RssSourceMessage;
import com.peploleum.insight.yummy.dto.source.twitter.TwitterSourceMessage;
import com.peploleum.insight.yummy.service.utils.NerResponseHandler;
import com.peploleum.insight.yummy.service.utils.RefGeoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
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

    private final Logger log = LoggerFactory.getLogger(NerService.class);
    private ObjectMapper mapperObj = new ObjectMapper();

    public NerService() {
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
            simpleRawData.setText(twitterSourceMessage.getText());
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
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final HttpEntity<NerJsonObjectQuery> entity = new HttpEntity<>(nerQuery, headers);
        final ResponseEntity<String> tResponseEntity = rt.exchange(this.urlner, HttpMethod.POST, entity, String.class);
        final NerJsonObjectResponse nerObjectResponse = mapperObj.readValue(tResponseEntity.getBody(), NerJsonObjectResponse.class);
        nerObjectResponse.setContent(tResponseEntity.getBody());
        log.debug("Received " + tResponseEntity.getBody());
        return nerObjectResponse;
    }

    private void createInRemoteServices(SimpleRawData simpleRawData, NerJsonObjectResponse nerObjectResponse) throws Exception {
        final NerResponseHandler responseHandler = new NerResponseHandler(nerObjectResponse, simpleRawData);
        log.info("Sending raw data to Insight");
        final RawDataDTO rawDataDto = responseHandler.getRawDataDto();

        String graphySourceId = null;
        final String rawDataId = this.insightClientService.create(rawDataDto);
        rawDataDto.setId(rawDataId);
        if (useGraph) {
            try {
                graphySourceId = this.graphyService.create(rawDataDto);
                rawDataDto.setExternalId(graphySourceId);
                this.insightClientService.update(rawDataDto);
            } catch (RestClientException e) {
                this.log.error("Failed to write in Graphy", e.getMessage());
                throw e;
            } catch (IOException e) {
                this.log.error("Failed to update in Insight", e.getMessage());
                throw e;
            }
        }
        log.info("Sent raw data " + rawDataId + " to Insight  ");
        final List<Object> insightEntities = responseHandler.getInsightEntities();
        log.info("Sending " + insightEntities.size() + " entities to Insight");

        String coordinates = null;
        if (this.useElasticSearch) {
            try {
                final List<String> collect = insightEntities.stream().filter((insightEntity) -> insightEntity instanceof LocationDTO).map((insightEntity) -> ((LocationDTO) insightEntity).getLocationName()).collect(Collectors.toList());
                for (String locationName : collect) {
                    this.log.info("Found locationName: " + locationName);
                    if (coordinates == null && locationName != null) {
                        coordinates = RefGeoUtils.getRefGeoCoordinates(locationName, this.elasticSearchService);
                    }
                }
                if (coordinates == null) {
                    this.log.warn("Found no coordinates among " + collect.size() + " locations");
                }
            } catch (Exception e) {
                this.log.error("Error While getting LocationName list", e);
            }
        }
        if (coordinates != null) {
            this.log.info("Updating raw data coordinates");
            setFieldValue(rawDataDto, "rawDataCoordinates", coordinates);
            this.insightClientService.update(rawDataDto);
        }
        for (Object o : insightEntities) {
            if (useGraph) {
                try {
                    if (coordinates != null) {
                        if (o instanceof BiographicsDTO)
                            setFieldValue(o, "biographicsCoordinates", coordinates);
                        else if (o instanceof EquipmentDTO)
                            setFieldValue(o, "equipmentCoordinates", coordinates);
                        else if (o instanceof EventDTO)
                            setFieldValue(o, "eventCoordinates", coordinates);
                        else if (o instanceof LocationDTO)
                            setFieldValue(o, "locationCoordinates", coordinates);
                        else if (o instanceof OrganisationDTO)
                            setFieldValue(o, "organisationCoordinates", coordinates);
                        else if (o instanceof RawDataDTO)
                            setFieldValue(o, "rawDataCoordinates", coordinates);
                    }
                    final String mongoId = this.insightClientService.create(o);
                    setFieldValue(o, "id", mongoId);
                    this.log.info("Created Insight Entity: " + o.toString());
                    final String janusId = this.graphyService.create(o);
                    setFieldValue(o, "externalId", janusId);
                    this.log.info("Created Graphy Entity: " + o.toString());
                    this.insightClientService.update(o);
                    this.log.info("Updated Insight Entity: " + o.toString());
                    this.log.info("Creating relation between " + getFieldValue(rawDataDto, "externalId") + " and " + getFieldValue(rawDataDto, "externalId"));
                    this.graphyService.createRelation(rawDataDto, o);
                } catch (Exception e) {
                    this.log.error("Failed to write in Graphy", e);
                }
            }
        }
        for (Object source : insightEntities) {
            for (Object target : insightEntities) {
                this.log.info("Creating relation between " + getFieldValue(source, "externalId") + " and " + getFieldValue(target, "externalId"));
                try {
                    this.graphyService.createRelation(source, target);
                } catch (Exception e) {
                    this.log.error("Failed to create relation in", e.getMessage());
                    throw e;
                }
            }
        }
    }

    private static void setFieldValue(Object dto, String fieldName, String externalIdValue) throws IllegalAccessException {
        Field sourceExternalIdField = org.springframework.util.ReflectionUtils.findField(dto.getClass(), fieldName);
        org.springframework.util.ReflectionUtils.makeAccessible(sourceExternalIdField);
        sourceExternalIdField.set(dto, externalIdValue);
    }

    private static String getFieldValue(Object dto, String fieldName) throws IllegalAccessException {
        Field field = org.springframework.util.ReflectionUtils.findField(dto.getClass(), fieldName);
        org.springframework.util.ReflectionUtils.makeAccessible(field);
        return field.get(dto).toString();
    }
}
