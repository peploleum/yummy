package com.peploleum.insight.yummy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableBinding(Source.class)
public class TimerSource {
    private final Logger log = LoggerFactory.getLogger(TimerSource.class);

    @Value("${format}")
    private String format;

    @Bean
    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "${fixed-delay}", maxMessagesPerPoll = "1"))
    public MessageSource<String> timerMessageSource() {
        return () -> {
            final String date = new SimpleDateFormat(this.format).format(new Date());
            final String message = "The date is " + date + " and isn't it a bit scary?";
            this.log.info("SENDING " + message);
            return new GenericMessage<>(message);

        };
    }

}
