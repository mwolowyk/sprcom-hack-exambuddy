package com.springer.hack.exambuddy.sementity;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "RELATED")
public class SemEntityRelation {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private SemEntity semEntityFrom;

    @EndNode
    private SemEntity semEntityTo;

    private Double similarity;

    private String commonLinks;

    public SemEntityRelation(SemEntity semEntityFrom, SemEntity semEntityTo) {
        this.semEntityFrom = semEntityFrom;
        this.semEntityTo = semEntityTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SemEntity getSemEntityFrom() {
        return semEntityFrom;
    }

    public void setSemEntityFrom(SemEntity semEntityFrom) {
        this.semEntityFrom = semEntityFrom;
    }

    public SemEntity getSemEntityTo() {
        return semEntityTo;
    }

    public void setSemEntityTo(SemEntity semEntityTo) {
        this.semEntityTo = semEntityTo;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SemEntityRelation that = (SemEntityRelation) o;

        if (semEntityFrom != null ? !semEntityFrom.equals(that.semEntityFrom) : that.semEntityFrom != null)
            return false;
        return semEntityTo != null ? semEntityTo.equals(that.semEntityTo) : that.semEntityTo == null;
    }

    @Override
    public int hashCode() {
        int result = semEntityFrom != null ? semEntityFrom.hashCode() : 0;
        result = 31 * result + (semEntityTo != null ? semEntityTo.hashCode() : 0);
        return result;
    }

    public String getCommonLinks() {
        return commonLinks;
    }

    public void setCommonLinks(String commonLinks) {
        this.commonLinks = commonLinks;
    }
}
