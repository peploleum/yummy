package com.peploleum.insight.yummy.dto;

/**
 * Created by cpoullot on 14/12/2018.
 */

import java.util.*;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "text",
        "steps"
})

public class NerJsonObjectQuery {

    @JsonProperty("text")
    private String text;
    @JsonProperty("steps")
    private List<String> steps = new ArrayList<>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("steps")
    public List<String> getSteps() {
        return steps;
    }

    @JsonProperty("steps")
    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public void addstep(String step)
    {
        this.steps.add(step);
    }

    public void addsteps(String steps)
    {
        String[] tabstep=steps.split(",");
        for (String step:tabstep
             ) {
            this.steps.add(step);
        }
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
