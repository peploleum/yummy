package com.peploleum.insight.yummy.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.source.elasticearch.EsMatchQuery;
import com.peploleum.insight.yummy.dto.source.elasticearch.EsResponse;
import com.peploleum.insight.yummy.dto.source.elasticearch.EsTermExactQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;
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
    @Value("${elasticsearch.index-name}")
    private String elasticsearchGazetteerIndex;


    private final Logger log = LoggerFactory.getLogger(ElasticSearchService.class);
    private ObjectMapper mapperObj = new ObjectMapper();
    private String searchUrl;
    private RestTemplate rt;
    private HttpHeaders headers;

    public ElasticSearchService() {
        this.mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @PostConstruct
    public void setup() {
        this.searchUrl = "http://" + this.elasticsearchHost + ":" + this.elasticsearchPort;
        this.rt = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(30)).setReadTimeout(Duration.ofSeconds(20)).build();
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        this.headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    /**
     * performs search query in gazetteer ES index and wraps response as an {@link EsResponse}
     *
     * @param locationName search criteria
     * @return en {@link EsResponse} wrapping the result
     * @throws IOException if result cannot be wrapped
     */
    public EsResponse submitElasticSearchGazetteerRequest(final String locationName) throws IOException {
        this.log.debug("submitting request for " + locationName);
       // final EsMatchQuery query = new EsMatchQuery("name", locationName);
        final EsTermExactQuery query=new EsTermExactQuery("name",locationName);
        final HttpEntity<String> entity = new HttpEntity<>(query.getContent(), headers);
        this.log.info("using endpoint " + this.searchUrl);
        this.log.info("sending  " + query.getContent());
        final ResponseEntity<String> tResponseEntity = rt.exchange(this.searchUrl + "/" + this.elasticsearchGazetteerIndex + "/_search", HttpMethod.POST, entity, String.class);
        log.debug("Received raw " + tResponseEntity.getBody());
        final EsResponse esObjectResponse = mapperObj.readValue(tResponseEntity.getBody(), EsResponse.class);
        esObjectResponse.setContent(tResponseEntity.getBody());
        log.debug("Received " + tResponseEntity.getBody());
        return esObjectResponse;
    }

    /**
     * performs search query for attribute and value couple in specified index
     *
     * @param attributeName  attribute key
     * @param attributeValue attribute value
     * @param indexName      name of the ES index to query
     * @return and {@link EsResponse} wrapping the ES response
     */
    public EsResponse getByNameCriteria(final String attributeName, final String attributeValue, final String indexName) {
        this.log.info("Getting by name criteria: " + attributeName + "=" + attributeValue + " [" + indexName + "]");
        final EsMatchQuery query = new EsMatchQuery(attributeName, attributeValue);
        final HttpEntity<String> entity = new HttpEntity<>(query.getContent(), headers);
        final String endpoint = this.searchUrl + "/" + indexName + "/_search";
        this.log.info("using endpoint " + endpoint);
        final ResponseEntity<EsResponse> tResponseEntity = rt.exchange(endpoint, HttpMethod.POST, entity, EsResponse.class);
        log.info("Received raw response " + tResponseEntity.getBody().toString());
        return tResponseEntity.getBody();
    }


}
