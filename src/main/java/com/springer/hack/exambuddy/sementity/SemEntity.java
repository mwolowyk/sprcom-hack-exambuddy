package com.springer.hack.exambuddy.sementity;


import com.springer.hack.exambuddy.common.BaseEntity;
import com.springer.hack.exambuddy.semcategory.SemCategory;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

@NodeEntity("Entity")
public class SemEntity extends BaseEntity {

    private String value;

    private String uri;

    private SemEntityType type;

    private String surfaceForm;

    private Double confidence;

    @Relationship(type = "RELATED", direction = Relationship.UNDIRECTED)
    private List<SemEntityRelation> relatedEntities = new ArrayList<>();

    @Relationship(type = "HAS_CATEGORY")
    private Set<SemCategory> semCategories = new HashSet<>();

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public SemEntityType getType() {
        return type;
    }

    public List<SemEntityRelation> getRelatedEntities() {
        return relatedEntities;
    }

    public void setRelatedEntities(List<SemEntityRelation> relatedEntities) {
        this.relatedEntities = relatedEntities;
    }

    public void setType(SemEntityType type) {
        this.type = type;
    }

    public String getSurfaceForm() {
        return surfaceForm;
    }

    public void setSurfaceForm(String surfaceForm) {
        this.surfaceForm = surfaceForm;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SemEntity semEntity = (SemEntity) o;

        if (!Objects.equals(value, semEntity.value)) return false;
        return type == semEntity.type;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public Set<SemCategory> getSemCategories() {
        return semCategories;
    }

    public void setSemCategories(Set<SemCategory> semCategories) {
        this.semCategories = semCategories;
    }

    public enum SemEntityType {
        CUSTOM, DBPEDIA
    }
}
