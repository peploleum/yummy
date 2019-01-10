package com.peploleum.insight.yummy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.config.TimerSource;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.source.RssSourceMessage;
import com.peploleum.insight.yummy.service.NerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YummyApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void ner() throws IOException {
        final Logger log = LoggerFactory.getLogger(TimerSource.class);
        ObjectMapper mapper = new ObjectMapper();
        final InputStream resourceAsStream = YummyApplicationTests.class.getResourceAsStream("/sample.json");
        RssSourceMessage mess = mapper.readValue(resourceAsStream, RssSourceMessage.class);
        new NerClient().doSend(mess, "http://localhost:9999/opener", "http://localhost:8080/api/", true);
    }

    @Test
    public void nerObjectResponseTest() throws IOException {
        final Logger log = LoggerFactory.getLogger(TimerSource.class);
        ObjectMapper mapper = new ObjectMapper();
        final InputStream resourceAsStream = YummyApplicationTests.class.getResourceAsStream("/bidou.json");
        final NerJsonObjectResponse nerJsonObjectResponse = mapper.readValue(resourceAsStream, NerJsonObjectResponse.class);
        assertThat(nerJsonObjectResponse.getText()).isNotEmpty();
        log.info(nerJsonObjectResponse.getText());
        assertThat(nerJsonObjectResponse.getTerms().get("t1") != null).isTrue();
    }

}
