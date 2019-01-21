package com.peploleum.insight.yummy.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.source.elasticSearch.EsMatchQuery;
import com.peploleum.insight.yummy.dto.source.elasticSearch.EsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;


/**
 * Created by cpoullot on 18/01/2019.
 */
@Service
public class ElasticSearchService {

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${elasticsearch.port}")
    private String elasticsearchPort;
    @Value("${elasticsearch.index}")
    private String elasticsearchIndex;

    private final Logger log = LoggerFactory.getLogger(ElasticSearchService.class);
    private ObjectMapper mapperObj = new ObjectMapper();
    private String searchUrl;

    public ElasticSearchService() {
        this.mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @PostConstruct
    public void setup() {
        this.searchUrl = "http://" + this.elasticsearchHost + ":" + this.elasticsearchPort + "/"+this.elasticsearchIndex+"/_search";
    }

    public EsResponse submitElasticSearchRequest(String locationName) throws IOException {
        final EsMatchQuery query=new EsMatchQuery("name",locationName);

        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final HttpEntity<String> entity = new HttpEntity<>(query.getContent(), headers);
        final ResponseEntity<String> tResponseEntity = rt.exchange(this.searchUrl, HttpMethod.POST, entity, String.class);
        final EsResponse esObjectResponse = mapperObj.readValue(tResponseEntity.getBody(), EsResponse.class);
        esObjectResponse.setContent(tResponseEntity.getBody());
        log.debug("Received " + tResponseEntity.getBody());
        return esObjectResponse;
    }


}
