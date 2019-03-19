package com.peploleum.insight.yummy.dto.source.elasticearch;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cpoullot on 18/01/2019.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "took",
        "timed_out",
        "_shards",
        "hits"
})
public class EsResponse {

    @JsonProperty("took")
    private Integer took;
    @JsonProperty("timed_out")
    private Boolean timedOut;
    @JsonProperty("_shards")
    private EsShards shards;
    @JsonProperty("hits")
    private EsHits hits;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    private String content;

    @JsonProperty("took")
    public Integer getTook() {
        return took;
    }

    @JsonProperty("took")
    public void setTook(Integer took) {
        this.took = took;
    }

    @JsonProperty("timed_out")
    public Boolean getTimedOut() {
        return timedOut;
    }

    @JsonProperty("timed_out")
    public void setTimedOut(Boolean timedOut) {
        this.timedOut = timedOut;
    }

    @JsonProperty("_shards")
    public EsShards getShards() {
        return shards;
    }

    @JsonProperty("_shards")
    public void setShards(EsShards shards) {
        this.shards = shards;
    }

    @JsonProperty("hits")
    public EsHits getHits() {
        return hits;
    }

    @JsonProperty("hits")
    public void setHits(EsHits hits) {
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


    //methode pour obtenir la liste des resultats (objet EsSource) de la requete elasticearch
    public List<EsSource> getSourceList() {
        List<EsSource> listSources = new ArrayList<>();
        //s'il y a des resultats
        if (getHits() != null) {
            //EsHits est une liste de EsHit
            EsHits hits = getHits();
            //s'il y a plus de zero hit
            if (hits.getTotal() > 0) {
                //chaque EsHit a 1 EsSource (resutats)
                for (EsHit hit : hits.getHits()
                ) {
                    listSources.add(hit.getSource());
                }
            }
        }
        return listSources;
    }
}
