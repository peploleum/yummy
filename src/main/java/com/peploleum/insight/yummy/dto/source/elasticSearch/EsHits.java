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
        "total",
        "max_score",
        "hits"
})
public class EsHits {

    @JsonProperty("total")
    private Integer total;
    @JsonProperty("max_score")
    private Double maxScore;
    @JsonProperty("hits")
    private List<EsHit> hits = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("total")
    public Integer getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(Integer total) {
        this.total = total;
    }

    @JsonProperty("max_score")
    public Double getMaxScore() {
        return maxScore;
    }

    @JsonProperty("max_score")
    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    @JsonProperty("hits")
    public List<EsHit> getHits() {
        return hits;
    }

    @JsonProperty("hits")
    public void setHits(List<EsHit> hits) {
        this.hits = hits;
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
