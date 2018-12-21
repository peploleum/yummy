package com.peploleum.insight.yummy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.config.TimerSource;
import com.peploleum.insight.yummy.dto.source.RssSourceMessage;
import com.peploleum.insight.yummy.service.NerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gFolgoas on 20/12/2018.
 */
public class NerTesting {

    public static void main(String[] args) {
        final Logger log = LoggerFactory.getLogger(TimerSource.class);
        ObjectMapper mapper = new ObjectMapper();
        final InputStream resourceAsStream = JsonTesting.class.getResourceAsStream("/sample.json");
        try {
            RssSourceMessage mess = mapper.readValue(resourceAsStream, RssSourceMessage.class);
            new NerClient().doSend(mess, "http://192.168.99.100:9999/opener", "http://192.168.99.100:9999:8080/api/", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
