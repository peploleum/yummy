package com.peploleum.insight.yummy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.RawDataDTO;
import com.peploleum.insight.yummy.dto.Rens;
import com.peploleum.insight.yummy.service.InsightClient;
import com.peploleum.insight.yummy.service.NerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableBinding(value = {Sink.class})
public class RawDataSink {
    private final Logger log = LoggerFactory.getLogger(RawDataSink.class);

    @Value("${urlner}")
    private String urlner;

    @Value("${urlinsight}")
    private String urlinsight;

    @Value("${format}")
    private String format;

    @StreamListener(Sink.INPUT)
    public void handle(String message) {
        try {
        ObjectMapper mapperObj = new ObjectMapper();
        Rens mess=mapperObj.readValue(message,Rens.class);
        final String display = "Received: " + message;
        log.info(display);
        new NerClient().doSend(mess, urlner,urlinsight);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
