package com.springer.hack.exambuddy.semcategory;

import com.springer.hack.exambuddy.common.BaseEntity;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity("Category")
public class SemCategory extends BaseEntity {

    private String uri;

    private String value;

    private Double entropy;

    @Relationship(type = "HAS_PARENT")
    private Set<SemCategory> semCategories = new HashSet<>();


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Double getEntropy() {
        return entropy;
    }

    public void setEntropy(Double entropy) {
        this.entropy = entropy;
    }

    public Set<SemCategory> getSemCategories() {
        return semCategories;
    }

    public void setSemCategories(Set<SemCategory> semCategories) {
        this.semCategories = semCategories;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SemCategory that = (SemCategory) o;

        return uri != null ? uri.equals(that.uri) : that.uri == null;
    }

    @Override
    public int hashCode() {
        return uri != null ? uri.hashCode() : 0;
    }
}
