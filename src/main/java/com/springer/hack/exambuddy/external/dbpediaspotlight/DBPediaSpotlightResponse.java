package com.springer.hack.exambuddy.external.dbpediaspotlight;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DBPediaSpotlightResponse {

    @JsonProperty("@text")
    private String text;

    @JsonProperty("@confidence")
    private String confidence;

    @JsonProperty("@support")
    private String support;

    @JsonProperty("@types")
    private String types;

    @JsonProperty("@sparql")
    private String sparql;

    @JsonProperty("@policy")
    private String policy;

    @JsonProperty("Resources")
    private List<Resource> resources = null;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getSparql() {
        return sparql;
    }

    public void setSparql(String sparql) {
        this.sparql = sparql;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}
