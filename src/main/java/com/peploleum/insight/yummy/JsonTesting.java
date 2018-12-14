package com.peploleum.insight.yummy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.config.TimerSource;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cpoullot on 14/12/2018.
 */
public class JsonTesting {
    public static void main(String[] args) {
        final Logger log = LoggerFactory.getLogger(TimerSource.class);
        ObjectMapper mapper = new ObjectMapper();
        final InputStream resourceAsStream = JsonTesting.class.getResourceAsStream("/bidou.json");
        try {
            NerJsonObjectResponse nerJsonObjectResponse = mapper.readValue(resourceAsStream, NerJsonObjectResponse.class);
            log.info(nerJsonObjectResponse.getText());
            nerJsonObjectResponse.getTerms().get("t1");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
