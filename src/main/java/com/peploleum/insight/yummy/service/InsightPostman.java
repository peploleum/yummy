package com.peploleum.insight.yummy.service;

import com.peploleum.insight.yummy.dto.RawDataDTO;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsightPostman {
    private final Logger log = LoggerFactory.getLogger(InsightPostman.class);


    public void doSend(RawDataDTO dto, List<String> cookies, String url) {
        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("X-XSRF-TOKEN", extractXsrf(cookies));
        for (String cookie : cookies) {
            headers.add("Cookie", cookie);
        }
        final HttpEntity<RawDataDTO> entity = new HttpEntity<>(dto, headers);
        final ResponseEntity<String> tResponseEntity = rt.exchange(url, HttpMethod.POST, entity, String.class);
        log.info("Received " + tResponseEntity);
    }


    public String account() {
        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> forEntity;
        try {
            forEntity = rt.exchange("http://localhost:8080/api/account", HttpMethod.GET, entity, String.class);
            log.info("Received " + forEntity);

        } catch (RestClientException e) {
            if (e instanceof HttpClientErrorException.Unauthorized) {
                log.info("Non autorisé");
                final List<String> cookies = ((HttpClientErrorException.Unauthorized) e).getResponseHeaders().get("Set-Cookie");
                final String actualCookie = extractXsrf(cookies);
                return actualCookie;
            }
        }
        return null;
    }

    public List<String> authent(String accountCookie) {
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
            final ResponseEntity<String> exchange = rt.exchange("http://localhost:8080/api/authentication", HttpMethod.POST, request, String.class);
            final List<String> cookies = exchange.getHeaders().get("Set-Cookie");
            final String xsrfValue = extractXsrf(cookies);
            final String jessionId = extractJessionId(cookies);
            if (xsrfValue != null && jessionId != null) {
                return cookies;
            }
            return null;
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

    private String extractXsrf(final List<String> cookies) {

        for (String cookie : cookies) {
            Pattern pattern = Pattern.compile("XSRF-TOKEN=(.*);[\\s]*(.*)");
            final Matcher matcher = pattern.matcher(cookie);
            if (matcher.matches()) {
                log.info("match: " + matcher.matches());
                return matcher.group(1);
            }
        }
        return null;
    }

    private String extractJessionId(final List<String> cookies) {
        for (String cookie : cookies) {
            Pattern pattern = Pattern.compile("JSESSIONID=(.*);[\\s]*(.*)");
            final Matcher matcher = pattern.matcher(cookie);
            if (matcher.matches()) {
                log.info("match: " + matcher.matches());
                return matcher.group(1);
            }
        }
        return null;
    }

    public void sendRaw( RawDataDTO dto, String urldataraw) {

        final InsightPostman insightPostman = new InsightPostman();
        final String accountCookie = insightPostman.account();
        if (accountCookie == null)
            return;
        final List<String> cookies = insightPostman.authent(accountCookie);
        if (cookies == null)
            return;
        insightPostman.doSend(dto, cookies, urldataraw);
    }

    public static void main(String[] args) {

        final InsightPostman insightPostman = new InsightPostman();
        final String accountCookie = insightPostman.account();
        if (accountCookie == null)
            return;
        final List<String> cookies = insightPostman.authent(accountCookie);
        if (cookies == null)
            return;
        RawDataDTO dto = new RawDataDTO();
        dto.setRawDataContent(UUID.randomUUID().toString());
        dto.setRawDataCreationDate(LocalDate.now());
        dto.setRawDataName(UUID.randomUUID().toString());
        insightPostman.doSend(dto, cookies, "http://localhost:8080/api/raw-data");
    }
}
