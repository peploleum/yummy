
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
    "description",
    "lastBuildDate",
    "language",
    "updatePeriod",
    "updateFrequency",
    "image",
    "item"
})
public class Channel {

    @JsonProperty("title")
    private String title;
    @JsonProperty("link")
    private List<Object> link = null;
    @JsonProperty("description")
    private String description;
    @JsonProperty("lastBuildDate")
    private String lastBuildDate;
    @JsonProperty("language")
    private String language;
    @JsonProperty("updatePeriod")
    private String updatePeriod;
    @JsonProperty("updateFrequency")
    private Integer updateFrequency;
    @JsonProperty("image")
    private Image image;
    @JsonProperty("item")
    private List<Item> item = null;
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
    public List<Object> getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(List<Object> link) {
        this.link = link;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("lastBuildDate")
    public String getLastBuildDate() {
        return lastBuildDate;
    }

    @JsonProperty("lastBuildDate")
    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("updatePeriod")
    public String getUpdatePeriod() {
        return updatePeriod;
    }

    @JsonProperty("updatePeriod")
    public void setUpdatePeriod(String updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    @JsonProperty("updateFrequency")
    public Integer getUpdateFrequency() {
        return updateFrequency;
    }

    @JsonProperty("updateFrequency")
    public void setUpdateFrequency(Integer updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    @JsonProperty("image")
    public Image getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(Image image) {
        this.image = image;
    }

    @JsonProperty("item")
    public List<Item> getItem() {
        return item;
    }

    @JsonProperty("item")
    public void setItem(List<Item> item) {
        this.item = item;
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
