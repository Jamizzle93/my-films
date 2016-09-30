package com.mysticwater.myfilms.model;

import java.util.ArrayList;
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
        "page",
        "results",
        "total_results",
        "total_pages"
})
public class FilmResults {

    @JsonProperty("page")
    private Integer page;
    @JsonProperty("results")
    private List<Film> results = new ArrayList<Film>();
    @JsonProperty("total_results")
    private Integer totalFilms;
    @JsonProperty("total_pages")
    private Integer totalPages;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The page
     */
    @JsonProperty("page")
    public Integer getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    @JsonProperty("page")
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return The results
     */
    @JsonProperty("results")
    public List<Film> getFilms() {
        return results;
    }

    /**
     * @param results The results
     */
    @JsonProperty("results")
    public void setFilms(List<Film> results) {
        this.results = results;
    }

    /**
     * @return The totalFilms
     */
    @JsonProperty("total_results")
    public Integer getTotalFilms() {
        return totalFilms;
    }

    /**
     * @param totalFilms The total_results
     */
    @JsonProperty("total_results")
    public void setTotalFilms(Integer totalFilms) {
        this.totalFilms = totalFilms;
    }

    /**
     * @return The totalPages
     */
    @JsonProperty("total_pages")
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages The total_pages
     */
    @JsonProperty("total_pages")
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
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
