package com.peploleum.insight.yummy.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.DummyPayload;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.Rens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class InsightClient {
    private final Logger log = LoggerFactory.getLogger(InsightClient.class);

    public void doSend() {
        ObjectMapper mapperObj = new ObjectMapper();
        mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            final DummyPayload dummyPayload = new DummyPayload();
            dummyPayload.setDate(new Date());
            dummyPayload.setMessage("Testing");
            final String dummyPayloadAsString = mapperObj.writeValueAsString(dummyPayload);
            log.info("Payload: " + dummyPayloadAsString);
            final RestTemplate rt = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            final HttpEntity<DummyPayload> entity = new HttpEntity<>(dummyPayload, headers);

            String url = "http://localhost:8182";

            final ResponseEntity<String> tResponseEntity = rt.exchange(url, HttpMethod.POST, entity, String.class);
            log.info("Received " + tResponseEntity);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void doSend(NerJsonObjectResponse nerResponse, String url)
    {
        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final HttpEntity<NerJsonObjectResponse> entity = new HttpEntity<>(nerResponse, headers);
        final ResponseEntity<String> tResponseEntity = rt.exchange(url, HttpMethod.POST, entity, String.class);
        log.info("Received " + tResponseEntity);
    }
}
