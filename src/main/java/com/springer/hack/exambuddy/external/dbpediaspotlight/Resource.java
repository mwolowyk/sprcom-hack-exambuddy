package com.springer.hack.exambuddy.external.dbpediaspotlight;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Resource {

    @JsonProperty("@URI")
    private String URI;

    @JsonProperty("@support")
    private String support;

    @JsonProperty("@types")
    private String types;

    @JsonProperty("@surfaceForm")
    private String surfaceForm;

    @JsonProperty("@offset")
    private String offset;

    @JsonProperty("@similarityScore")
    private String similarityScore;

    @JsonProperty("@percentageOfSecondRank")
    private String percentageOfSecondRank;

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
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

    public String getSurfaceForm() {
        return surfaceForm;
    }

    public void setSurfaceForm(String surfaceForm) {
        this.surfaceForm = surfaceForm;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(String similarityScore) {
        this.similarityScore = similarityScore;
    }

    public String getPercentageOfSecondRank() {
        return percentageOfSecondRank;
    }

    public void setPercentageOfSecondRank(String percentageOfSecondRank) {
        this.percentageOfSecondRank = percentageOfSecondRank;
    }
}
