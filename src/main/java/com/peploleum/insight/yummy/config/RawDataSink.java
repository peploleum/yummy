package com.peploleum.insight.yummy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.SourceMessage;
import com.peploleum.insight.yummy.dto.source.RssSourceMessage;
import com.peploleum.insight.yummy.service.NerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

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

    @Value("${ner}")
    private boolean ner;

    @Value("${mock}")
    private boolean mock;

    @StreamListener(Sink.INPUT)
    public void handle(String message) {
        try {
            ObjectMapper mapperObj = new ObjectMapper();
            if (mock) {
                SourceMessage mess = mapperObj.readValue(message, SourceMessage.class);
                final String display = "Received: " + message;
                log.info(display);
                new NerClient().doSendSourceMessage(mess, urlner, urlinsight, ner);
            } else {
                RssSourceMessage mess = mapperObj.readValue(message, RssSourceMessage.class);
                final String display = "Received: " + message;
                log.info(display);
                new NerClient().doSend(mess, urlner, urlinsight, ner);
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
