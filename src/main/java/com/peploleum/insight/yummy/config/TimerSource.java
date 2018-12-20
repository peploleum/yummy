package com.peploleum.insight.yummy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.SourceMessage;
import com.peploleum.insight.yummy.dto.source.RssSourceMessage;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
//@EnableBinding(Source.class)
public class TimerSource {
    private final Logger log = LoggerFactory.getLogger(TimerSource.class);

    @Value("${format}")
    private String format;

 /*   @Bean
    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "${fixed-delay}", maxMessagesPerPoll = "1"))
    public MessageSource<String> timerMessageSource() {
        return () -> {
            final ObjectMapper om = new ObjectMapper();
            String val = "";
            try {
                InputStream i=TimerSource.class.getResourceAsStream("/exemple.txt");
                BufferedReader r = new BufferedReader(new InputStreamReader(i));

                // reads each line
                String l;
                while((l = r.readLine()) != null) {
                    val = val + l;
                }
                i.close();

                this.log.info("SENDING " + val);
                return new GenericMessage<>(val);

               // final RssSourceMessage rssSourceMessage = om.readValue(TimerSource.class.getResourceAsStream("/sample.json"), RssSourceMessage.class);
               // final String message = om.writeValueAsString(rssSourceMessage);

                //this.log.info("SENDING " + message);
                //return new GenericMessage<>(message);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            return new GenericMessage<>("");
        };
    }*/

}
