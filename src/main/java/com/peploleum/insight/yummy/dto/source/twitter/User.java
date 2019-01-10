
package com.peploleum.insight.yummy.dto.source.twitter;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "id_str",
        "name",
        "screen_name",
        "location"
})
public class User {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("id_str")
    private String idStr;
    @JsonProperty("name")
    private String name;
    @JsonProperty("screen_name")
    private String screenName;
    @JsonProperty("location")
    private String location;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("id_str")
    public String getIdStr() {
        return idStr;
    }

    @JsonProperty("id_str")
    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("screen_name")
    public String getScreenName() {
        return screenName;
    }

    @JsonProperty("screen_name")
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
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
