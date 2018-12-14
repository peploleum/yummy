package com.peploleum.insight.yummy.dto;

/**
 * Created by cpoullot on 14/12/2018.
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "term"
})

public class Terms {
    @JsonProperty("term")
    private Map<String, Term> terms;

    @JsonProperty("term")
    public Map<String, Term> getTerms() {
        return terms;
    }
    @JsonProperty("term")
    public void setTerms(Map<String, Term> terms) {
        this.terms = terms;
    }
}
