package com.peploleum.insight.yummy.dto.source.ner;

import com.fasterxml.jackson.annotation.*;
import com.peploleum.insight.yummy.dto.source.ner.NerEntitiesType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gFolgoas on 21/12/2018.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "eid",
        "type"
})
public class Entity {

    @JsonProperty("eid")
    private String eid;
    @JsonProperty("type")
    private NerEntitiesType type;
    @JsonProperty("text")
    private String text;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("eid")
    public String getEid() {
        return eid;
    }

    @JsonProperty("eid")
    public void setEid(String eid) {
        this.eid = eid;
    }

    @JsonProperty("type")
    public NerEntitiesType getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(NerEntitiesType type) {
        this.type = type;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
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
