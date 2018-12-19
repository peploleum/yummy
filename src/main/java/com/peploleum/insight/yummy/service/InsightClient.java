package com.peploleum.insight.yummy.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.DummyPayload;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.RawDataDTO;
import com.peploleum.insight.yummy.dto.Rens;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

public class InsightClient {
    private final Logger log = LoggerFactory.getLogger(InsightClient.class);

    public void doSend() {
        ObjectMapper mapperObj = new ObjectMapper();
        mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            final DummyPayload dummyPayload = new DummyPayload();
            dummyPayload.setDate(new Date());
            dummyPayload.setMessage("Testing");
            final String dummyPayloadAsString = mapperObj.writeValueAsString(dummyPayload);
            log.info("Payload: " + dummyPayloadAsString);
            final RestTemplate rt = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            final HttpEntity<DummyPayload> entity = new HttpEntity<>(dummyPayload, headers);

            String url = "http://localhost:8182";

            final ResponseEntity<String> tResponseEntity = rt.exchange(url, HttpMethod.POST, entity, String.class);
            log.info("Received " + tResponseEntity);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void getToken(String url)
    {
        try {
            final StringEntity loginVM = new StringEntity("{\"password\": \"admin\",\"rememberMe\": true,\"username\": \"admin\"}");
           /* final RestTemplate rt = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            final HttpEntity<StringEntity> entity = new HttpEntity<>(loginVM, headers);
            final ResponseEntity<String> tResponseEntity = rt.exchange(url, HttpMethod.POST, entity, String.class);
            log.info("Received " + tResponseEntity);
*/

            final CloseableHttpClient client = HttpClientBuilder.create().build();
            final HttpPost tokenRequest = new HttpPost(url);
            tokenRequest.addHeader("Content-Type", "application/json");
            tokenRequest.addHeader("Accept", "application/json");
            tokenRequest.setEntity(loginVM);

            HttpResponse responseForToken = null;
            try {
                responseForToken = client.execute(tokenRequest);
                log.info("Received " + responseForToken.toString());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            final String[] particules = responseForToken.toString().split(",");
            for (final String p : particules)
            {
                if (p.startsWith("Set-Cookie: XSRF-TOKEN="))
                {

                }
            }

        }catch (final UnsupportedEncodingException e1)
        {
            log.info(e1.getMessage(),e1);
        }

    }

    public void doSend(RawDataDTO dataRawDto,  String url, String tokenHttp)
    {
        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        if (tokenHttp!=null)
        {
            headers.add("X-XSRF-TOKEN",tokenHttp);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_PROBLEM_JSON));

        final HttpEntity<RawDataDTO> entity = new HttpEntity<>(dataRawDto, headers);
        try {
            final ResponseEntity<String> tResponseEntity = rt.exchange(url, HttpMethod.POST, entity, String.class);
            log.info("Received " + tResponseEntity);
        }catch (Exception e)
        {
            log.info(e.getMessage(),e);
        }

    }
}
