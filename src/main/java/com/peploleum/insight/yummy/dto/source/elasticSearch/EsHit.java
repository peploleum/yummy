package com.peploleum.insight.yummy.dto.source.elasticSearch;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cpoullot on 21/01/2019.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "_index",
        "_type",
        "_id",
        "_score",
        "_source"
})
public class EsHit {

    @JsonProperty("_index")
    private String index;
    @JsonProperty("_type")
    private String type;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_score")
    private Double score;
    @JsonProperty("_source")
    private EsSource source;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("_index")
    public String getIndex() {
        return index;
    }

    @JsonProperty("_index")
    public void setIndex(String index) {
        this.index = index;
    }

    @JsonProperty("_type")
    public String getType() {
        return type;
    }

    @JsonProperty("_type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("_score")
    public Double getScore() {
        return score;
    }

    @JsonProperty("_score")
    public void setScore(Double score) {
        this.score = score;
    }

    @JsonProperty("_source")
    public EsSource getSource() {
        return source;
    }

    @JsonProperty("_source")
    public void setSource(EsSource source) {
        this.source = source;
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
