package com.peploleum.insight.yummy.dto.source.ner;

/**
 * Created by cpoullot on 14/12/2018.
 */

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "lemma",
        "text",
        "pos",
        "morphofeat"
})
public class Term {

    @JsonProperty("type")
    private String type;
    @JsonProperty("lemma")
    private String lemma;
    @JsonProperty("text")
    private String text;
    @JsonProperty("pos")
    private String pos;
    @JsonProperty("morphofeat")
    private String morphofeat;
    private Integer offset;
    private Integer length;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("lemma")
    public String getLemma() {
        return lemma;
    }

    @JsonProperty("lemma")
    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("pos")
    public String getPos() {
        return pos;
    }

    @JsonProperty("pos")
    public void setPos(String pos) {
        this.pos = pos;
    }

    @JsonProperty("morphofeat")
    public String getMorphofeat() {
        return morphofeat;
    }

    @JsonProperty("morphofeat")
    public void setMorphofeat(String morphofeat) {
        this.morphofeat = morphofeat;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
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
