package com.peploleum.insight.yummy.service;

import com.peploleum.insight.yummy.dto.entities.RawDataDTO;
import com.peploleum.insight.yummy.service.utils.InsightHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class InsightPostman {
    private final Logger log = LoggerFactory.getLogger(InsightPostman.class);
    private String INSIGHT_APP_API_URI;

    public InsightPostman(final String insightAppUrl) {
        this.INSIGHT_APP_API_URI = insightAppUrl;
    }

    public void sendToInsight(Object entity) {
        this.log.info("Sending Entity");
        List<String> cookies = this.getCookies();
        if (cookies == null) {
            return;
        }
        this.log.info("session cookie received");
        this.doSend(entity, cookies);
    }


    public void doSend(Object dto, List<String> cookies) {
        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = InsightHttpUtils.getHttpJsonHeader(cookies);
        final ResponseEntity<String> tResponseEntity;
        try {
            tResponseEntity = rt.exchange(this.INSIGHT_APP_API_URI + InsightHttpUtils.getInsigthMethodUrl(dto), HttpMethod.POST,
                    new HttpEntity<>(dto, headers), String.class);
            log.info("Received " + tResponseEntity);
        } catch (RestClientException e) {
            this.log.error(e.getMessage(), e);
        }
    }

    private String account() {
        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> forEntity;
        try {
            forEntity = rt.exchange(INSIGHT_APP_API_URI + "account", HttpMethod.GET, entity, String.class);
            log.info("Received " + forEntity);

        } catch (RestClientException e) {
            if (e instanceof HttpClientErrorException.Unauthorized) {
                log.info("Non autorisé");
                final List<String> cookies = ((HttpClientErrorException.Unauthorized) e).getResponseHeaders().get("Set-Cookie");
                final String actualCookie = InsightHttpUtils.extractXsrf(cookies);
                return actualCookie;
            }
        }
        return null;
    }

    private List<String> authent(String accountCookie) {
        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("j_username", "admin");
        map.add("j_password", "admin");
        map.add("remember-me:", "true");
        map.add("submit", "Login");
        headers.set("X-XSRF-TOKEN", accountCookie);
        headers.add("Cookie", "XSRF-TOKEN=" + accountCookie);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        try {
            final ResponseEntity<String> exchange = rt.exchange(INSIGHT_APP_API_URI + "authentication", HttpMethod.POST, request, String.class);
            final List<String> cookies = exchange.getHeaders().get("Set-Cookie");
            final String xsrfValue = InsightHttpUtils.extractXsrf(cookies);
            final String jessionId = InsightHttpUtils.extractJessionId(cookies);
            if (xsrfValue != null && jessionId != null) {
                return cookies;
            }
            return null;
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

    private List<String> getCookies() {
        final String accountCookie = this.account();
        if (accountCookie == null)
            return null;
        this.log.info("account cookie received");
        return this.authent(accountCookie);
    }

    public static void main(String[] args) {
        final InsightPostman insightPostman = new InsightPostman("http://localhost:8080/api/");
        RawDataDTO dto = new RawDataDTO();
        dto.setRawDataContent(UUID.randomUUID().toString());
        dto.setRawDataCreationDate(LocalDate.now());
        dto.setRawDataName(UUID.randomUUID().toString());
        insightPostman.sendToInsight(dto);
    }


}
