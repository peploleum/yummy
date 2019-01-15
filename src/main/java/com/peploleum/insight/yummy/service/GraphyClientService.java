package com.peploleum.insight.yummy.service;

import com.peploleum.insight.yummy.dto.entities.graphy.RelationDTO;
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

import java.io.IOException;

@Service
public class GraphyClientService {

    @Value("${graph.enabled}")
    private boolean graphEnabled;

    @Value("${graph.host}")
    private String graphHost;

    @Value("${graph.port}")
    private int graphPort;

    private final Logger log = LoggerFactory.getLogger(GraphyClientService.class);

    public void sendToGraphy(Object entity) throws IOException {
        this.log.debug("Sending Entity " + entity);
        this.doSend(entity);
    }

    public void sendRelationToGraphy(String idSource, String idTarget, String sourceType, String targetType) throws IOException {
        this.log.debug("Sending relation " + idSource + " to " + idTarget + " typeSource: " + sourceType + " typeTarget: " + targetType);
        this.doSendRelation(idSource, idTarget, sourceType, targetType);
    }

    private void doSendRelation(String idSource, String idTarget, String sourceType, String targetType) {
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
            tResponseEntity = rt.exchange("http://" + this.graphHost + ":" + this.graphPort + "/api/relation", HttpMethod.POST,
                    new HttpEntity<>(relationDTO, headers), String.class);
            log.debug("Received " + tResponseEntity);
        } catch (RestClientException e) {
            this.log.warn("Failed to send entity");
            this.log.debug(e.getMessage(), e);
            throw e;
        }
    }

    private void doSend(final Object dto) throws RestClientException {
        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = InsightHttpUtils.getBasicHeaders();
        final ResponseEntity<String> tResponseEntity;
        try {
            final String insigthMethodUrl = InsightHttpUtils.getInsigthMethodUrl(dto);
            if (insigthMethodUrl.isEmpty()) {
                this.log.warn("Failed to find endpoint for entity");
                return;
            } else {
                this.log.debug("Sending " + dto.toString());
            }
            tResponseEntity = rt.exchange("http://" + this.graphHost + ":" + this.graphPort + "/api/" + insigthMethodUrl, HttpMethod.POST,
                    new HttpEntity<>(dto, headers), String.class);
            log.debug("Received " + tResponseEntity);
        } catch (RestClientException e) {
            this.log.warn("Failed to send entity");
            this.log.debug(e.getMessage(), e);
            throw e;
        }
    }
}
