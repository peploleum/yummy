package com.peploleum.insight.yummy.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.NerJsonObjectQuery;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.source.SimpleRawData;
import com.peploleum.insight.yummy.dto.source.rss.Item;
import com.peploleum.insight.yummy.dto.source.rss.RssSourceMessage;
import com.peploleum.insight.yummy.dto.source.twitter.TwitterSourceMessage;
import com.peploleum.insight.yummy.service.utils.NerResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

@Service
public class NerClientService {

    @Value("${urlner}")
    private String urlner;

    @Value("${urlinsight}")
    private String urlinsight;

    @Value("${format}")
    private String format;

    @Value("${ner}")
    private boolean useNer;

    @Autowired
    private InsightClientService insightClientService;

    private final Logger log = LoggerFactory.getLogger(NerClientService.class);
    private ObjectMapper mapperObj = new ObjectMapper();

    public NerClientService() {
        this.mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public void doSend(final Object message) throws IOException {
        if (message instanceof RssSourceMessage) {
            try {
                this.log.info("Processing RSS message");
                final RssSourceMessage rssSourceMessage = (RssSourceMessage) message;
                int cpt = 0;
                this.log.info("Items to process: " + rssSourceMessage.getChannel().getItem().size());
                for (Item item : rssSourceMessage.getChannel().getItem()) {
                    final String nerCandidate = (item.getDescription() != null && !item.getDescription().isEmpty()) ? item.getDescription() : item.getTitle();
                    final SimpleRawData simpleRawData = SimpleRawData.fromRssSourceMessage(rssSourceMessage);
                    simpleRawData.setText(nerCandidate);
                    NerJsonObjectResponse nerJsonObjectResponse = null;
                    if (this.useNer) {
                        nerJsonObjectResponse = submitNerRequest(simpleRawData);
                    }
                    submitInsightRequest(simpleRawData, nerJsonObjectResponse);
                    cpt++;
                }
                log.debug("Number of Items processed : " + cpt);
            } catch (IOException e) {
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
            submitInsightRequest(simpleRawData, nerJsonObjectResponse);
        }
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

    private void submitInsightRequest(SimpleRawData simpleRawData, NerJsonObjectResponse nerObjectResponse) throws IOException {
        final NerResponseHandler responseHandler = new NerResponseHandler(nerObjectResponse, simpleRawData);
        this.insightClientService.sendToInsight(responseHandler.getRawDataDto());
        for (Object o : responseHandler.getInsightEntities()) {
            this.insightClientService.sendToInsight(o);
        }
    }
}
