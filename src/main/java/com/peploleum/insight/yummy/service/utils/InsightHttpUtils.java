package com.peploleum.insight.yummy.service.utils;

import com.peploleum.insight.yummy.dto.entities.insight.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsightHttpUtils {

    public static HttpHeaders getHttpJsonHeader(List<String> cookies) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        if (cookies == null || cookies.isEmpty()) {
            return headers;
        }
        headers.add("X-XSRF-TOKEN", extractXsrf(cookies));
        for (String cookie : cookies) {
            headers.add("Cookie", cookie);
        }
        return headers;
    }

    public static HttpHeaders getBasicHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public static String getInsigthMethodUrl(Object o) {
        if (o instanceof Biographics) {
            return "biographics";
        } else if (o instanceof Location) {
            return "locations";
        } else if (o instanceof Organisation) {
            return "organisations";
        } else if (o instanceof RawData) {
            return "raw-data";
        } else if (o instanceof Event) {
            return "events";
        } else if (o instanceof Equipment) {
            return "equipment";
        } else {
            return null;
        }
    }

    public static String extractXsrf(final List<String> cookies) {
        for (String cookie : cookies) {
            Pattern pattern = Pattern.compile("XSRF-TOKEN=(.*);[\\s]*(.*)");
            final Matcher matcher = pattern.matcher(cookie);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    public static String extractJessionId(final List<String> cookies) {
        for (String cookie : cookies) {
            Pattern pattern = Pattern.compile("JSESSIONID=(.*);[\\s]*(.*)");
            final Matcher matcher = pattern.matcher(cookie);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        return null;
    }
}
