
package com.peploleum.insight.yummy.dto.source;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "title",
    "link",
    "pubDate",
    "creator",
    "category",
    "guid",
    "description",
    "enclosure"
})
public class Item {

    @JsonProperty("title")
    private String title;
    @JsonProperty("link")
    private String link;
    @JsonProperty("pubDate")
    private String pubDate;
    @JsonProperty("creator")
    private String creator;
    @JsonProperty("category")
    private List<String> category = null;
    @JsonProperty("guid")
    private Guid guid;
    @JsonProperty("description")
    private String description;
    @JsonProperty("enclosure")
    private Enclosure enclosure;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("pubDate")
    public String getPubDate() {
        return pubDate;
    }

    @JsonProperty("pubDate")
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @JsonProperty("creator")
    public String getCreator() {
        return creator;
    }

    @JsonProperty("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @JsonProperty("category")
    public List<String> getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(List<String> category) {
        this.category = category;
    }

    @JsonProperty("guid")
    public Guid getGuid() {
        return guid;
    }

    @JsonProperty("guid")
    public void setGuid(Guid guid) {
        this.guid = guid;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("enclosure")
    public Enclosure getEnclosure() {
        return enclosure;
    }

    @JsonProperty("enclosure")
    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
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
