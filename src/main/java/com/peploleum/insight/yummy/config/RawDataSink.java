package com.peploleum.insight.yummy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(value = {Sink.class})
public class RawDataSink {
    private final Logger log = LoggerFactory.getLogger(RawDataSink.class);

    @Value("${format}")
    private String format;

    @StreamListener(Sink.INPUT)
    public void handle(String message) {
        final String display = "Received: " + message;
        System.out.println(display);
        log.info(display);
    }

}
