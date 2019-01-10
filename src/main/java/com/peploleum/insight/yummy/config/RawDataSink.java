package com.peploleum.insight.yummy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.source.rss.RssSourceMessage;
import com.peploleum.insight.yummy.dto.source.twitter.TwitterSourceMessage;
import com.peploleum.insight.yummy.service.NerClientService;
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
    private NerClientService nerClientService;

    @StreamListener(Sink.INPUT)
    public void handle(String message) {
        log.info("Yummy received raw message");
        log.debug("message content is: " + message);
        final ObjectMapper mapperObj = new ObjectMapper();
        if (message.contains("created_at")) {
            try {
                final TwitterSourceMessage twitterSourceMessage = mapperObj.readValue(message, TwitterSourceMessage.class);
                log.info("Sucessfully parsed TwitterMessage.");
                this.nerClientService.doSend(twitterSourceMessage);
            } catch (IOException e1) {
                this.log.error(e1.getMessage(), e1);
            }
        } else {
            try {
                final RssSourceMessage rssSourceMessage = mapperObj.readValue(message, RssSourceMessage.class);
                log.info("Sucessfully parsed RssMessage.");
                final boolean success = this.nerClientService.doSend(rssSourceMessage);
                if (!success) {
                    log.warn("Failed to process message : " + message);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
