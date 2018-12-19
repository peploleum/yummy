

package com.peploleum.insight.yummy.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "@version",
        "@timestamp",
        "link",
        "soureData",
        "description",
        "dateTraiment",
        "title"
})
public class SourceMessage {

    @JsonProperty("@version")
    private String version;
    @JsonProperty("@timestamp")
    private String timestamp;
    @JsonProperty("link")
    private List<String> link = null;
    @JsonProperty("soureData")
    private String soureData;
    @JsonProperty("description")
    private List<String> description = null;
    @JsonProperty("dateTraiment")
    private String dateTraiment;
    @JsonProperty("title")
    private List<String> title = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("@version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("@version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("@timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("@timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("link")
    public List<String> getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(List<String> link) {
        this.link = link;
    }

    @JsonProperty("soureData")
    public String getSoureData() {
        return soureData;
    }

    @JsonProperty("soureData")
    public void setSoureData(String soureData) {
        this.soureData = soureData;
    }

    @JsonProperty("description")
    public List<String> getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(List<String> description) {
        this.description = description;
    }

    @JsonProperty("dateTraiment")
    public String getDateTraiment() {
        return dateTraiment;
    }

    @JsonProperty("dateTraiment")
    public void setDateTraiment(String dateTraiment) {
        this.dateTraiment = dateTraiment;
    }

    @JsonProperty("title")
    public List<String> getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(List<String> title) {
        this.title = title;
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