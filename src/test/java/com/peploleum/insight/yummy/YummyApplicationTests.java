package com.peploleum.insight.yummy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.entities.insight.BiographicsDTO;
import com.peploleum.insight.yummy.dto.entities.insight.RawDataDTO;
import com.peploleum.insight.yummy.dto.source.SimpleRawData;
import com.peploleum.insight.yummy.dto.source.rss.RssSourceMessage;
import com.peploleum.insight.yummy.dto.source.twitter.TwitterSourceMessage;
import com.peploleum.insight.yummy.service.InsightService;
import com.peploleum.insight.yummy.service.NerService;
import com.peploleum.insight.yummy.service.utils.NerResponseHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YummyApplicationTests {

    private final Logger log = LoggerFactory.getLogger(YummyApplicationTests.class);

    @Autowired
    private NerService nerService;

    @Autowired
    private InsightService insightClientService;


    @Test
    public void contextLoads() {
    }

    @Test
    public void nerJsonObjectResponseTest() throws IOException {
        final Logger log = LoggerFactory.getLogger(YummyApplicationTests.class);
        ObjectMapper mapper = new ObjectMapper();
        final InputStream resourceAsStream = YummyApplicationTests.class.getResourceAsStream("/bidou.json");
        final NerJsonObjectResponse nerJsonObjectResponse = mapper.readValue(resourceAsStream, NerJsonObjectResponse.class);
        assertThat(nerJsonObjectResponse.getText()).isNotEmpty();
        log.info(nerJsonObjectResponse.getText());
        assertThat(nerJsonObjectResponse.getTerms().get("t1") != null).isTrue();
    }

    @Test
    public void twitterSourceMessageTest() throws IOException {
        final Logger log = LoggerFactory.getLogger(YummyApplicationTests.class);
        ObjectMapper mapper = new ObjectMapper();
        final InputStream resourceAsStream = YummyApplicationTests.class.getResourceAsStream("/sample_twitter.json");
        final TwitterSourceMessage twitterSourceMessage = mapper.readValue(resourceAsStream, TwitterSourceMessage.class);
        assertThat(twitterSourceMessage.getText()).isNotEmpty();
        log.info(twitterSourceMessage.getText());
        assertThat(twitterSourceMessage.getCreatedAt()).isNotNull();
    }

    @Test
    public void twitterMessageToNerTest() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final InputStream resourceAsStream = YummyApplicationTests.class.getResourceAsStream("/sample_twitter.json");
        final TwitterSourceMessage twitterSourceMessage = mapper.readValue(resourceAsStream, TwitterSourceMessage.class);
        final SimpleRawData simpleRawData = SimpleRawData.fromTwitterSourceMessage(twitterSourceMessage);
        final NerJsonObjectResponse nerJsonObjectResponse = this.nerService.submitNerRequest(simpleRawData);
        final String content = nerJsonObjectResponse.getContent();
        Assert.assertNotNull(content);
        this.log.info(content);
        final List<Object> objects = NerResponseHandler.extractInsightEntites(nerJsonObjectResponse);
        for (final Object object : objects) {
            this.log.info(object.toString());
        }
    }

    @Test
    public void rssMessageToNerToInsightTest() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final InputStream resourceAsStream = YummyApplicationTests.class.getResourceAsStream("/sample.json");
        final RssSourceMessage mess = mapper.readValue(resourceAsStream, RssSourceMessage.class);
        this.nerService.doSend(mess);
    }

    @Test
    public void insightPostmanTest() throws IOException {
        final RawDataDTO rawDataDTO = new RawDataDTO();
        rawDataDTO.setRawDataName("test");
        rawDataDTO.setRawDataContent("test");
        rawDataDTO.setRawDataCreationDate(Instant.now());
        final BiographicsDTO biographicsDTO = new BiographicsDTO();
        biographicsDTO.setBiographicsFirstname("testFirstName");
        biographicsDTO.setBiographicsName("testName");
        final String idRawData = this.insightClientService.create(rawDataDTO);
        final String idBio = this.insightClientService.create(biographicsDTO);
        Assert.assertNotNull(idRawData);
        Assert.assertNotNull(idBio);
    }

    @Test
    public void graphPostTest() throws IOException {
        final RawDataDTO rawDataDTO = new RawDataDTO();
        rawDataDTO.setRawDataContent("test");
        rawDataDTO.setRawDataCreationDate(Instant.now());
        this.insightClientService.create(rawDataDTO);
    }

}
