package com.peploleum.insight.yummy.dto.source.elasticSearch;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cpoullot on 21/01/2019.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "total",
        "successful",
        "skipped",
        "failed"
})
public class EsShards {

    @JsonProperty("total")
    private Integer total;
    @JsonProperty("successful")
    private Integer successful;
    @JsonProperty("skipped")
    private Integer skipped;
    @JsonProperty("failed")
    private Integer failed;
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

    @JsonProperty("successful")
    public Integer getSuccessful() {
        return successful;
    }

    @JsonProperty("successful")
    public void setSuccessful(Integer successful) {
        this.successful = successful;
    }

    @JsonProperty("skipped")
    public Integer getSkipped() {
        return skipped;
    }

    @JsonProperty("skipped")
    public void setSkipped(Integer skipped) {
        this.skipped = skipped;
    }

    @JsonProperty("failed")
    public Integer getFailed() {
        return failed;
    }

    @JsonProperty("failed")
    public void setFailed(Integer failed) {
        this.failed = failed;
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
