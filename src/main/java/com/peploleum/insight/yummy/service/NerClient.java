package com.peploleum.insight.yummy.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.NerJsonObjectQuery;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.Rens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

/**
 * Created by cpoullot on 14/12/2018.
 */
public class NerClient {

    private final Logger log = LoggerFactory.getLogger(InsightClient.class);

    public void doSend(Rens message, String url) {
        ObjectMapper mapperObj = new ObjectMapper();
        mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {

            NerJsonObjectQuery nerQuery=new NerJsonObjectQuery();
            nerQuery.addsteps("identify_language,tokenize,pos,ner");
            nerQuery.setText(message.getTitle().get(0));
            final String dummyPayloadAsString = mapperObj.writeValueAsString(nerQuery);
            log.info("Payload: " + dummyPayloadAsString);
            final RestTemplate rt = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            final HttpEntity<NerJsonObjectQuery> entity = new HttpEntity<>(nerQuery, headers);

            final ResponseEntity<String> tResponseEntity = rt.exchange(url, HttpMethod.POST, entity, String.class);
            NerJsonObjectResponse jsonResponse=mapperObj.readValue(tResponseEntity.getBody(), NerJsonObjectResponse.class);
            log.info("Received " + tResponseEntity);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
