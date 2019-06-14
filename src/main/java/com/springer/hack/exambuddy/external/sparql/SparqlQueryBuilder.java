package com.springer.hack.exambuddy.external.sparql;

public class SparqlQueryBuilder {

    public static String getWikipageLinksQueryOf(int depth, String uri, String var) {
        String sparqlQueryString = null;
        if (depth == 0) {
            sparqlQueryString = String.format("SELECT DISTINCT ?%s FROM <http://dbpedia.org/page_links> WHERE { <%s> dbo:wikiPageWikiLink ?%s  . }", var, uri, var);
        } else if (depth == 1) {
            sparqlQueryString = String.format("SELECT DISTINCT ?%s FROM <http://dbpedia.org/page_links> WHERE { <%s> dbo:wikiPageWikiLink ?o.  ?o dbo:wikiPageWikiLink  ?%s . }", var, uri, var);
        } else if (depth == 2) {
            sparqlQueryString = String.format("SELECT DISTINCT ?%s FROM <http://dbpedia.org/page_links> WHERE { <%s> dbo:wikiPageWikiLink  ?o . ?o dbo:wikiPageWikiLink ?o2 . ?o2 dbo:wikiPageWikiLink ?%s  . }", var, uri, var);
        }
        return sparqlQueryString;
    }

    public static String getCategoryQuery(int depth, String uri, String... vars) {
        String sparqlQueryString = null;
        String catRef = vars[0];
        String superCatRef = vars.length > 1 ? vars[1] : "";
        String superSuperCatRef = vars.length > 2 ? vars[2] : "";
        if (depth == 0) {
            sparqlQueryString = String.format("SELECT DISTINCT ?%s WHERE { <%s> dct:subject ?%s . }", catRef, uri, catRef);
        } else if (depth == 1) {
            sparqlQueryString = String.format("SELECT DISTINCT ?%s ?%s WHERE { <%s> dct:subject ?%s. ?%s skos:broader ?%s .}", catRef, superCatRef, uri, catRef, catRef, superCatRef);
        } else if (depth == 2) {
            sparqlQueryString = String.format("SELECT DISTINCT ?%s ?%s ?%s WHERE { <%s> dct:subject ?%s. ?%s skos:broader ?%s . ?%s skos:broader ?%s .}", catRef, superCatRef, superSuperCatRef, uri, catRef, catRef, superCatRef, superCatRef, superSuperCatRef);
        }
        return sparqlQueryString;
    }

}
