package com.peploleum.insight.yummy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.source.rawtext.RawTextMessage;
import com.peploleum.insight.yummy.dto.source.rss.RssSourceMessage;
import com.peploleum.insight.yummy.dto.source.twitter.TwitterSourceMessage;
import com.peploleum.insight.yummy.service.NerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBinding(value = {Sink.class})
public class RawDataSink {
    private final Logger log = LoggerFactory.getLogger(RawDataSink.class);

    @Autowired
    private NerService nerService;

    /**
     * Fonction de test pour import de fichier txt brut
     * Le contenu est inclu dans un JSON, puis mappé dans le DTO RawTextMessage
     */
    // @EventListener(ApplicationReadyEvent.class)
    public void readFile() {
        try {
            String message = new String(Files.readAllBytes(Paths.get("D:\\Users\\gfolgoas\\Desktop\\rawtxt_sample.txt")));

            Map<String, Object> temp = new HashMap<>();
            temp.put("rawText", message);
            temp.put("title", "Texte brut de test");
            temp.put("documentType", "txt");
            String jsonRawTxt = new ObjectMapper().writeValueAsString(temp);

            this.handle(jsonRawTxt);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @StreamListener(Sink.INPUT)
    public void handle(String message) {
        log.info("Yummy received raw message");
        log.debug("message content is: " + message);

//        Map<String, Object> jsonContent = new HashMap<>();
        final ObjectMapper mapperObj = new ObjectMapper();
//        try {
//            jsonContent = mapperObj.readValue(message, new TypeReference<Map<String, String>>() {
//            });
//        } catch (Exception e) {
//            this.log.error(e.getMessage(), e);
//        }
//
//        if (jsonContent == null) {
//            this.log.warn("Failed to parse json message");
//            return;
//        }

        try {
            if (message.contains("created_at")) {
                final TwitterSourceMessage twitterSourceMessage = mapperObj.readValue(message, TwitterSourceMessage.class);
                log.info("Sucessfully parsed TwitterMessage.");
                this.nerService.doSend(twitterSourceMessage);

            } else if (message.contains("channel")) {
                final RssSourceMessage rssSourceMessage = mapperObj.readValue(message, RssSourceMessage.class);
                log.info("Sucessfully parsed RssMessage.");
                final boolean success = this.nerService.doSend(rssSourceMessage);
                if (!success) {
                    log.warn("Failed to process message : " + message);
                }
            } else if (message.contains("rawText")) {
                final RawTextMessage rawTxtMessage = mapperObj.readValue(message, RawTextMessage.class);
                log.info("Sucessfully parsed RssMessage.");
                final boolean success = this.nerService.doSend(rawTxtMessage);
                if (!success) {
                    log.warn("Failed to process message : " + message);
                }
            }
        } catch (Exception e1) {
            this.log.error(e1.getMessage(), e1);
        }
    }

}
