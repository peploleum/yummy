package com.peploleum.insight.yummy.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.NerJsonObjectQuery;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.RawDataDTO;
import com.peploleum.insight.yummy.dto.Rens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by cpoullot on 14/12/2018.
 */
public class NerClient {

    private final Logger log = LoggerFactory.getLogger(InsightClient.class);

    public void doSend(Rens message, String urlner, String urlinsight) {
        ObjectMapper mapperObj = new ObjectMapper();
        mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {

            int cpt=0;
            for (String title :message.getTitle()
                 ) {
                NerJsonObjectQuery nerQuery=new NerJsonObjectQuery();
                nerQuery.addsteps("identify_language,tokenize,pos,ner");
                nerQuery.setText(message.getTitle().get(cpt));
                final String dummyPayloadAsString = mapperObj.writeValueAsString(nerQuery);
                log.info("Payload: " + dummyPayloadAsString);
                final RestTemplate rt = new RestTemplate();
                final HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                final HttpEntity<NerJsonObjectQuery> entity = new HttpEntity<>(nerQuery, headers);
                final ResponseEntity<String> tResponseEntity = rt.exchange(urlner, HttpMethod.POST, entity, String.class);
                NerJsonObjectResponse nerObjectRespone=mapperObj.readValue(tResponseEntity.getBody(), NerJsonObjectResponse.class);
                nerObjectRespone.setContent(tResponseEntity.getBody());
                //RawDataDTO dataRaw=createDatarow(nerObjectRespone,message.getTitle().get(cpt),message.getSoureData(),message.getLink().get(cpt), message.getDateTraiment());
                RawDataDTO dataRaw=createDatarowNoDate(nerObjectRespone,message.getTitle().get(cpt),message.getSoureData(),message.getLink().get(cpt));
                new InsightPostman().sendRaw(dataRaw,urlinsight);
                log.info("Received " + tResponseEntity.getBody());
                cpt++;
            }


        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }


    public void doSendOne(Rens message, String urlner, String urlinsight) {
        ObjectMapper mapperObj = new ObjectMapper();
        mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        RawDataDTO dataRaw=null;
        try {

            int cpt=0;

                NerJsonObjectQuery nerQuery=new NerJsonObjectQuery();
                nerQuery.addsteps("identify_language,tokenize,pos,ner");
                nerQuery.setText(message.getTitle().get(cpt));
                final String dummyPayloadAsString = mapperObj.writeValueAsString(nerQuery);
                log.info("Payload: " + dummyPayloadAsString);
                final RestTemplate rt = new RestTemplate();
                final HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                final HttpEntity<NerJsonObjectQuery> entity = new HttpEntity<>(nerQuery, headers);
                final ResponseEntity<String> tResponseEntity = rt.exchange(urlner, HttpMethod.POST, entity, String.class);
                NerJsonObjectResponse nerObjectRespone=mapperObj.readValue(tResponseEntity.getBody(), NerJsonObjectResponse.class);
                nerObjectRespone.setContent(tResponseEntity.getBody());
                //dataRaw=createDatarow(nerObjectRespone,message.getTitle().get(cpt),message.getSoureData(),message.getLink().get(cpt), message.getDateTraiment());
                dataRaw=createDatarowNoDate(nerObjectRespone,message.getTitle().get(cpt),message.getSoureData(),message.getLink().get(cpt));

                log.info("Received " + tResponseEntity.getBody());
                new InsightPostman().sendRaw(dataRaw,urlinsight);


        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


    public RawDataDTO createDatarowNoDate(NerJsonObjectResponse nerResponse, String name, String SourceName, String SourceUri)
    {

        RawDataDTO dataRaw=new RawDataDTO();
        log.info("RAW DATA NAME "+name);
        dataRaw.setRawDataName(name);
        dataRaw.setRawDataSourceName(SourceName);
        dataRaw.setRawDataSourceUri(SourceUri);
        dataRaw.setRawDataAnnotations(nerResponse.getContent());
        dataRaw.setRawDataContent(nerResponse.getText());
        dataRaw.setRawDataCreationDate(LocalDate.now());
        dataRaw.setRawDataType(nerResponse.getLanguage());
        return dataRaw;
    }

    public RawDataDTO createDatarow(NerJsonObjectResponse nerResponse, String name, String SourceName, String SourceUri, String dateTraiment)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        //convert String to LocalDate
        LocalDate localDateTraitement = LocalDate.parse(dateTraiment, formatter);

        RawDataDTO dataRaw=new RawDataDTO();
        dataRaw.setRawDataName(name);
        dataRaw.setRawDataSourceName(SourceName);
        dataRaw.setRawDataSourceUri(SourceUri);
        dataRaw.setRawDataAnnotations(nerResponse.getContent());
        dataRaw.setRawDataContent(nerResponse.getText());
        dataRaw.setRawDataExtractedDate(localDateTraitement);
        dataRaw.setRawDataCreationDate(LocalDate.now());
        dataRaw.setRawDataType(nerResponse.getLanguage());
        return dataRaw;
    }
}
