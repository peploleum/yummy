package com.peploleum.insight.yummy.service;

import com.peploleum.insight.yummy.dto.entities.insight.RelationDTO;
import com.peploleum.insight.yummy.service.utils.InsightHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;

@Service
public class GraphyService {

    private String apiRootUrl;
    @Value("${graph.enabled}")
    private boolean graphEnabled;

    @Value("${graph.host}")
    private String graphHost;

    @Value("${graph.port}")
    private int graphPort;

    private final Logger log = LoggerFactory.getLogger(GraphyService.class);

    public GraphyService() {
    }

    @PostConstruct
    public void setup() {
        this.apiRootUrl = "http://" + this.graphHost + ":" + this.graphPort + "/api/";
    }

    public String create(Object entity) throws RestClientException {
        this.log.debug("Sending Entity " + entity);
        return this.doSend(entity);
    }

    public void sendRelationToGraphy(String idSource, String idTarget, String sourceType, String targetType) throws RestClientException {
        this.log.debug("Sending relation " + idSource + " to " + idTarget + " typeSource: " + sourceType + " typeTarget: " + targetType);
        this.doSendRelation(idSource, idTarget, sourceType, targetType);
    }


    public void createRelation(Object sourceDTO, Object targetDTO) throws Exception {
        try {
            final Object sourceExternalIdValue = extractFieldValue(sourceDTO);
            final Object targetExternalIdValue = extractFieldValue(targetDTO);
            this.doSendRelation(sourceExternalIdValue.toString(), targetExternalIdValue.toString(), TypeResolver.resolve(sourceDTO), TypeResolver.resolve(targetDTO));
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
            throw e;
        }
    }

    private Object extractFieldValue(Object dto) throws IllegalAccessException {
        Field sourceExternalIdField = org.springframework.util.ReflectionUtils.findField(dto.getClass(), "externalId");
        org.springframework.util.ReflectionUtils.makeAccessible(sourceExternalIdField);
        return sourceExternalIdField.get(dto);
    }

    private String doSendRelation(String idSource, String idTarget, String sourceType, String targetType) throws RestClientException {
        this.log.info("Creating relation between " + idSource + "/" + sourceType + " and " + idTarget + "/" + targetType);
        final RelationDTO relationDTO = new RelationDTO();
        relationDTO.setIdJanusSource(idSource);
        relationDTO.setIdJanusCible(idTarget);
        relationDTO.setName("linked to");
        relationDTO.setTypeSource(sourceType);
        relationDTO.setTypeCible(targetType);

        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = InsightHttpUtils.getBasicHeaders();
        final ResponseEntity<String> tResponseEntity;
        try {
            tResponseEntity = rt.exchange(this.apiRootUrl + "relation", HttpMethod.POST,
                    new HttpEntity<>(relationDTO, headers), String.class);
            log.debug("Received " + tResponseEntity.getBody());
            return tResponseEntity.getBody();
        } catch (RestClientException e) {
            this.log.warn("Failed to send entity");
            this.log.debug(e.getMessage(), e);
            throw e;
        }
    }

    private String doSend(final Object dto) throws RestClientException {
        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = InsightHttpUtils.getBasicHeaders();
        final ResponseEntity<String> tResponseEntity;
        try {
            final String graphyEnpointSuffix = InsightHttpUtils.getInsigthMethodUrl(dto);
            if (graphyEnpointSuffix.isEmpty()) {
                this.log.warn("Failed to find endpoint for entity");
                return null;
            }
            final String url = this.apiRootUrl + graphyEnpointSuffix;
            this.log.warn("Sending " + dto.toString() + " to " + url);
            tResponseEntity = rt.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(dto, headers), String.class);
            log.debug("Received " + tResponseEntity.getBody());
            return tResponseEntity.getBody();
        } catch (RestClientException e) {
            this.log.warn("Failed to send entity");
            this.log.debug(e.getMessage(), e);
            throw e;
        }
    }
}

class TypeResolver {
    public static String resolve(Object object) {
        return object.getClass().getSimpleName().substring(0, object.getClass().getSimpleName().length() - 3);
    }
}
