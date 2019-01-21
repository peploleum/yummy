package com.peploleum.insight.yummy.dto.source.elasticSearch;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cpoullot on 21/01/2019.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "host",
        "population",
        "longitude",
        "@timestamp",
        "countrycode",
        "name",
        "@version",
        "location",
        "asciiname",
        "latitude",
        "message",
        "path"
})

public class EsSource {

    @JsonProperty("host")
    private String host;
    @JsonProperty("population")
    private String population;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("@timestamp")
    private String timestamp;
    @JsonProperty("countrycode")
    private String countrycode;
    @JsonProperty("name")
    private String name;
    @JsonProperty("@version")
    private String version;
    @JsonProperty("location")
    private List<Double> location = null;
    @JsonProperty("asciiname")
    private String asciiname;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("message")
    private String message;
    @JsonProperty("path")
    private String path;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("host")
    public String getHost() {
        return host;
    }

    @JsonProperty("host")
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty("population")
    public String getPopulation() {
        return population;
    }

    @JsonProperty("population")
    public void setPopulation(String population) {
        this.population = population;
    }

    @JsonProperty("longitude")
    public String getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("@timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("@timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("countrycode")
    public String getCountrycode() {
        return countrycode;
    }

    @JsonProperty("countrycode")
    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("@version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("@version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("location")
    public List<Double> getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(List<Double> location) {
        this.location = location;
    }

    @JsonProperty("asciiname")
    public String getAsciiname() {
        return asciiname;
    }

    @JsonProperty("asciiname")
    public void setAsciiname(String asciiname) {
        this.asciiname = asciiname;
    }

    @JsonProperty("latitude")
    public String getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
