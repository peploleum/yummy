package com.peploleum.insight.yummy.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.NerJsonObjectQuery;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.source.Item;
import com.peploleum.insight.yummy.dto.source.RssSourceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

public class NerClient {

    private final Logger log = LoggerFactory.getLogger(InsightClient.class);

    public void doSend(RssSourceMessage message, String urlner, String urlinsight, final boolean useNer) {
        final ObjectMapper mapperObj = new ObjectMapper();
        mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {

            int cpt = 0;
            for (Item item : message.getChannel().getItem()) {
                NerJsonObjectQuery nerQuery = new NerJsonObjectQuery();
                nerQuery.addsteps("identify_language,tokenize,pos,ner");
                final String nerCandidate = (item.getDescription() != null && !item.getDescription().isEmpty()) ? item.getDescription() : item.getTitle();
                nerQuery.setText(nerCandidate);
                NerJsonObjectResponse nerObjectResponse = null;

                if (useNer) {
                    final String dummyPayloadAsString = mapperObj.writeValueAsString(nerQuery);
                    log.info("Payload: " + dummyPayloadAsString);
                    final RestTemplate rt = new RestTemplate();
                    final HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    final HttpEntity<NerJsonObjectQuery> entity = new HttpEntity<>(nerQuery, headers);
                    final ResponseEntity<String> tResponseEntity = rt.exchange(urlner, HttpMethod.POST, entity, String.class);

                    nerObjectResponse = mapperObj.readValue(tResponseEntity.getBody(), NerJsonObjectResponse.class);
                    nerObjectResponse.setContent(tResponseEntity.getBody());
                    //RawDataDTO dataRaw=createDatarow(nerObjectResponse,message.getTitle().get(cpt),message.getSoureData(),message.getLink().get(cpt), message.getDateTraiment());
                    log.info("Received " + tResponseEntity.getBody());
                }

                final NerResponseHandler responseHandler = new NerResponseHandler(nerObjectResponse, message, nerCandidate);

                InsightPostman insightPostman = new InsightPostman(urlinsight);
                insightPostman.sendToInsight(responseHandler.getRawDataDto());
                for (Object o : responseHandler.getInsightEntities()) {
                    insightPostman.sendToInsight(o);
                }

                cpt++;
            }
            log.info("Number of Items processed : " + cpt);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }
}
