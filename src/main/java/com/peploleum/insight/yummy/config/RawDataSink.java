package com.peploleum.insight.yummy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.source.RssSourceMessage;
import com.peploleum.insight.yummy.service.NerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@EnableBinding(value = {Sink.class})
public class RawDataSink {
    private final Logger log = LoggerFactory.getLogger(RawDataSink.class);

    @Autowired
    private NerClient nerClient;

    @StreamListener(Sink.INPUT)
    public void handle(String message) {
        try {
            final ObjectMapper mapperObj = new ObjectMapper();
            final RssSourceMessage mess = mapperObj.readValue(message, RssSourceMessage.class);
            final String display = "Received: " + message;
            log.info(display);
            this.nerClient.doSend(mess);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
