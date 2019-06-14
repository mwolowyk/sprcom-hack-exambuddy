package com.springer.hack.exambuddy.external.sparql;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class SparqlQueryBuilderTest {

    private String uri = "http://dbpedia.org/resource/MacBook_Pro";

    @Test
    public void testQueryForDepth0() {
        String query = SparqlQueryBuilder.getWikipageLinksQueryOf(0, uri, "ref");
        String expectedQuery1 = "SELECT DISTINCT ?ref FROM <http://dbpedia.org/page_links> WHERE { <http://dbpedia.org/resource/MacBook_Pro> dbo:wikiPageWikiLink ?ref  . }";
        assertThat(query).isEqualTo(expectedQuery1);
    }

    @Test
    public void testQueryForDepth1() {
        String query = SparqlQueryBuilder.getWikipageLinksQueryOf(1, uri, "ref");
        String expectedQuery = "SELECT DISTINCT ?ref FROM <http://dbpedia.org/page_links> WHERE { <http://dbpedia.org/resource/MacBook_Pro> dbo:wikiPageWikiLink ?o.  ?o dbo:wikiPageWikiLink  ?ref . }";
        assertThat(query).isEqualTo(expectedQuery);
    }

    @Test
    public void testQueryForDepth2() {
        String query = SparqlQueryBuilder.getWikipageLinksQueryOf(2, uri, "ref");
        String expectedQuery = "SELECT DISTINCT ?ref FROM <http://dbpedia.org/page_links> WHERE { <http://dbpedia.org/resource/MacBook_Pro> dbo:wikiPageWikiLink  ?o . ?o dbo:wikiPageWikiLink ?o2 . ?o2 dbo:wikiPageWikiLink ?ref  . }";
        assertThat(query).isEqualTo(expectedQuery);
    }

    @Test
    public void testCategoryQueryForDepth0() {
        String query = SparqlQueryBuilder.getCategoryQuery(0, uri, "cat");
        String expectedQuery = "SELECT DISTINCT ?cat WHERE { <http://dbpedia.org/resource/MacBook_Pro> dct:subject ?cat . }";
        assertThat(query).isEqualTo(expectedQuery);
    }

    @Test
    public void testCategoryQueryForDepth1() {
        String query = SparqlQueryBuilder.getCategoryQuery(1, uri, "cat", "supercat");
        String expectedQuery = "SELECT DISTINCT ?cat ?supercat WHERE { <http://dbpedia.org/resource/MacBook_Pro> dct:subject ?cat. ?cat skos:broader ?supercat .}";
        assertThat(query).isEqualTo(expectedQuery);
    }

    @Test
    public void testCategoryQueryForDepth2() {
        String query = SparqlQueryBuilder.getCategoryQuery(2, uri, "cat", "supercat", "supersupercat");
        String expectedQuery = "SELECT DISTINCT ?cat ?supercat ?supersupercat WHERE { <http://dbpedia.org/resource/MacBook_Pro> dct:subject ?cat. ?cat skos:broader ?supercat . ?supercat skos:broader ?supersupercat .}";
        assertThat(query).isEqualTo(expectedQuery);
    }
}
