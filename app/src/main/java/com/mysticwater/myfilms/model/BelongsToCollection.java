package com.mysticwater.myfilms.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "poster_path",
        "backdrop_path"
})
public class BelongsToCollection {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The posterPath
     */
    @JsonProperty("poster_path")
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * @param posterPath The poster_path
     */
    @JsonProperty("poster_path")
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     * @return The backdropPath
     */
    @JsonProperty("backdrop_path")
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * @param backdropPath The backdrop_path
     */
    @JsonProperty("backdrop_path")
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
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
