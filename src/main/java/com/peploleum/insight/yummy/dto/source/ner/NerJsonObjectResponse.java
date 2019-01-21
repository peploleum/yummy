package com.peploleum.insight.yummy.dto.source.ner;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "text",
        "language",
        "terms",
        "lp",
        "entities"
})
public class NerJsonObjectResponse {

    @JsonProperty("text")
    private String text;
    @JsonProperty("language")
    private String language;
    @JsonProperty("terms")
    private Map<String, Term> terms;
    @JsonProperty("entities")
    private Map<String, Entity> entities;
    @JsonProperty("lp")
    private List<Lp> lp = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    private String content;

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("terms")
    public Map<String, Term> getTerms() {
        return terms;
    }

    @JsonProperty("terms")
    public void setTerms(Map<String, Term> terms) {
        this.terms = terms;
    }

    @JsonProperty("lp")
    public List<Lp> getLp() {
        return lp;
    }

    @JsonProperty("lp")
    public void setLp(List<Lp> lp) {
        this.lp = lp;
    }

    @JsonProperty("entities")
    public Map<String, Entity> getEntities() {
        return entities;
    }

    @JsonProperty("entities")
    public void setEntities(Map<String, Entity> entities) {
        this.entities = entities;
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
