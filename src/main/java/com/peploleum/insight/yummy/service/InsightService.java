package com.peploleum.insight.yummy.service;

import com.peploleum.insight.yummy.dto.entities.Identifiers;
import com.peploleum.insight.yummy.dto.entities.insight.Relation;
import com.peploleum.insight.yummy.service.utils.InsightHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class InsightService {

    @Value("${urlinsight}")
    private String urlinsight;

    private final Logger log = LoggerFactory.getLogger(InsightService.class);
    private List<String> cookies;
    private RestTemplate restTemplate;

    @PostConstruct
    private void onConstruct() {
        initRestTemplate();
    }

    private void initRestTemplate() {
        this.restTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(30)).setReadTimeout(Duration.ofSeconds(20)).build();
        this.cookies = this.generateCookies();
    }

    @Recover
    public Identifiers recover(org.springframework.web.client.ResourceAccessException e, Object entity) throws IOException {
        try {
            return this.doSend(entity, HttpMethod.POST, true);
        } catch (Exception e1) {
            this.log.error(e1.getMessage(), e1);
            throw new IOException("entity creation failed after all attempts.");
        }
    }

    @Recover
    public Identifiers recover(org.springframework.web.client.HttpClientErrorException e, Object entity) throws IOException {
        try {
            return this.doSend(entity, HttpMethod.POST, true);
        } catch (Exception e1) {
            this.log.error(e1.getMessage(), e1);
            throw new IOException("entity creation failed after all attempts.");
        }
    }

    @Retryable(maxAttempts = 2, value = {HttpClientErrorException.class, ResourceAccessException.class}, backoff = @Backoff(delay = 5000))
    public Identifiers create(Object entity) {
        this.log.debug("Creating Entity");
        return this.doSend(entity, HttpMethod.POST, false);
    }

    public Identifiers update(Object entity) {
        this.log.debug("Sending Entity");
        return this.doSend(entity, HttpMethod.PUT, false);
    }

    private Identifiers doSend(final Object dto, final HttpMethod method, final boolean needAuth) throws RestClientException {
        if (needAuth) {
            initRestTemplate();
        }
        final HttpHeaders headers = InsightHttpUtils.getHttpJsonHeader(this.cookies);
        final ResponseEntity<Object> tResponseEntity;
        try {
            final String insigthMethodUrl = InsightHttpUtils.getInsigthMethodUrl(dto);
            if (insigthMethodUrl.isEmpty()) {
                this.log.warn("Failed to find endpoint for entity");
                return null;
            } else {
                this.log.debug("Sending " + dto.toString());
            }
            tResponseEntity = this.restTemplate.exchange(this.urlinsight + insigthMethodUrl, method,
                    new HttpEntity<>(dto, headers), Object.class);
            log.debug("Received " + tResponseEntity);
            final LinkedHashMap body = (LinkedHashMap) tResponseEntity.getBody();
            final String id = (String) body.get("id");
            final String externalId = (String) body.get("externalId");
            final Identifiers identifiers = new Identifiers();
            identifiers.setId(id);
            identifiers.setExternalId(externalId);
            return identifiers;
        } catch (Exception e) {
            this.log.warn("Failed to send entity");
            this.log.debug(e.getMessage(), e);
            throw e;
        }
    }

    private String account() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            final ResponseEntity<String> forEntity = this.restTemplate.exchange(this.urlinsight + "account", HttpMethod.GET, entity, String.class);
            log.info("Received " + forEntity);
            log.info("Extracting cookie");
            final List<String> cookies = forEntity.getHeaders().get("Set-Cookie");
            final String actualCookie = InsightHttpUtils.extractXsrf(cookies);
            log.info("Extracted cookie: " + actualCookie);
            return actualCookie;
        } catch (RestClientException e) {
            if (e instanceof HttpClientErrorException.Unauthorized) {
                log.warn("Unauthorized. Need to retrieve session cookie for future requests.");
                final List<String> cookies = ((HttpClientErrorException.Unauthorized) e).getResponseHeaders().get("Set-Cookie");
                final String actualCookie = InsightHttpUtils.extractXsrf(cookies);
                return actualCookie;
            } else {
                log.error("Failed to contact account service. ", e);
            }
        }
        return null;
    }


    private List<String> authent(String accountCookie) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("j_username", "admin");
        map.add("j_password", "admin");
        map.add("remember-me", "true");
        map.add("submit", "Login");
        if (accountCookie != null) {
            headers.set("X-XSRF-TOKEN", accountCookie);
            headers.add("Cookie", "XSRF-TOKEN=" + accountCookie);
        }
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        try {
            final ResponseEntity<String> exchange = this.restTemplate.exchange(this.urlinsight + "authentication", HttpMethod.POST, request, String.class);
            final List<String> cookies = exchange.getHeaders().get("Set-Cookie");
            final String xsrfValue = InsightHttpUtils.extractXsrf(cookies);
            final String jessionId = InsightHttpUtils.extractJessionId(cookies);
            if (xsrfValue != null && jessionId != null) {
                this.log.warn("Cookies found");
                return cookies;
            }
            return null;
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private List<String> generateCookies() {
        this.log.warn("Generating cookies");
        final String accountCookie = this.account();
        this.log.warn("Account cookie: " + accountCookie);
        if (accountCookie == null)
            return null;
        this.log.warn("Account cookie received. Anthenticating");
        final List<String> cookies = this.authent(accountCookie);
        return cookies;
    }

    public void createRelation(Object sourceDTO, Object targetDTO) throws Exception {
        try {
            final Object sourceExternalIdValue = extractFieldValue(sourceDTO);
            final Object targetExternalIdValue = extractFieldValue(targetDTO);
            this.doSendRelation(sourceExternalIdValue.toString(), targetExternalIdValue.toString(), TypeResolver.resolve(sourceDTO), TypeResolver.resolve(targetDTO));
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
            throw e;
        }
    }

    private Object extractFieldValue(Object dto) throws IllegalAccessException {
        Field sourceExternalIdField = org.springframework.util.ReflectionUtils.findField(dto.getClass(), "externalId");
        org.springframework.util.ReflectionUtils.makeAccessible(sourceExternalIdField);
        return sourceExternalIdField.get(dto);
    }

    private String doSendRelation(String idSource, String idTarget, String sourceType, String targetType) throws RestClientException {
        final HttpHeaders headers = InsightHttpUtils.getHttpJsonHeader(this.cookies);
        this.log.info("Creating relation between " + idSource + "/" + sourceType + " and " + idTarget + "/" + targetType);
        final Relation relation = new Relation();
        relation.setIdJanusSource(idSource);
        relation.setIdJanusCible(idTarget);
        relation.setName("linked to");
        relation.setTypeSource(sourceType);
        relation.setTypeCible(targetType);

        final ResponseEntity<String> tResponseEntity;
        try {
            tResponseEntity = this.restTemplate.exchange(this.urlinsight + "graph/relation", HttpMethod.POST,
                    new HttpEntity<>(relation, headers), String.class);
            log.debug("Received " + tResponseEntity.getBody());
            return tResponseEntity.getBody();
        } catch (RestClientException e) {
            this.log.warn("Failed to send relation");
            this.log.debug(e.getMessage(), e);
            throw e;
        }
    }
}

class TypeResolver {
    public static String resolve(Object object) {
        return object.getClass().getSimpleName();
    }
}