package com.springer.hack.exambuddy.external.dbpedia;


import com.google.common.collect.Lists;
import com.springer.hack.exambuddy.external.BaseRestService;
import com.springer.hack.exambuddy.external.sparql.SparqlQueryBuilder;
import com.springer.hack.exambuddy.sementity.SemEntity;
import com.springer.hack.exambuddy.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@EnableConfigurationProperties(DBPediaSparqlProperties.class)
public class DBPediaSparqlService extends BaseRestService {

    private RestTemplate restTemplate = new RestTemplate();

    private static Logger log = LoggerFactory.getLogger(DBPediaSparqlService.class);

    private Map<String, List<String>> entityLinks;

    private final DBPediaSparqlProperties properties;

    @Autowired
    public DBPediaSparqlService(DBPediaSparqlProperties properties) {
        super(properties);
        this.properties = properties;
        this.entityLinks = new ConcurrentHashMap<>();
    }

    @Async
    public CompletableFuture<List<String>> fetchWikipageLinks(SemEntity semEntity, int depth) {
        String key = "ref";
        var query = SparqlQueryBuilder.getWikipageLinksQueryOf(depth, semEntity.getUri(), key);
        return executeSparqlQueryForEntity(semEntity, query);
    }

    private CompletableFuture<List<String>> executeSparqlQueryForEntity(SemEntity semEntity, String query) {
        var result = new CompletableFuture<List<String>>();
        CompletableFuture<String> stringCompletableFuture = executeSparqlQuery(query);
        stringCompletableFuture.thenApply(s -> {
            ArrayList<String> links = Lists.newArrayList(s.split("\n"));
            links.remove(0);
            log.info("Got {} links for uri {} " , links.size(), semEntity.getValue());
            return result.complete(links);
        });
        return result;
    }

    private CompletableFuture<String> executeSparqlQuery(String query) {
        var result = new CompletableFuture<String>();
        var url = buildUrl("/", Map.of("query", query, "format", "text/tab-separated-values", "timeout", 30000));

        try {
            log.debug("Calling url {}", url);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getRequest(), String.class);
            result.complete(response.getBody());
        } catch (Exception e) {
            log.error("Got error from url {} {}", url, e.getMessage());
            result.completeExceptionally(e);
        }

        return result;
    }


    public Double calculateSimilarity(SemEntity semEntity1, SemEntity semEntity2, int depth, CollectionUtils.SimilarityCalculationType calculationType) {

        List<String> wikiPageLinks1 = getWikipageLinksForEntity(semEntity1, fetchWikipageLinks(semEntity1, depth));
        List<String> wikiPageLinks2 = getWikipageLinksForEntity(semEntity2, fetchWikipageLinks(semEntity2, depth));

        var intersection = CollectionUtils.getIntersection(wikiPageLinks1, wikiPageLinks2);

        log.info("Intersection size {}", intersection.size());

        return CollectionUtils.calculateSimilarity(wikiPageLinks1, wikiPageLinks2, calculationType);
    }

    private List<String> getWikipageLinksForEntity(SemEntity semEntity, CompletableFuture<List<String>> wikipageLinks) {
        List<String> wikiPageLinks1 = entityLinks.get(semEntity.getValue());
        if (wikiPageLinks1 == null) {
            log.info("Links for uri {} not found. Calculating", semEntity.getValue());
            wikiPageLinks1 = wikipageLinks.join();

            log.info("Got {} for uri {}", wikiPageLinks1.size(), semEntity.getValue());

            entityLinks.put(semEntity.getValue(), wikiPageLinks1);
        }

        return wikiPageLinks1;
    }

    protected HttpEntity<String> getRequest() {
        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<>(headers);
    }

    public CompletableFuture<List<String>> fetchSemCategories(SemEntity semEntity, int depth) {
        String[] keys = {"cat", "supercat"};
        var query = SparqlQueryBuilder.getCategoryQuery(depth, semEntity.getUri(), keys);
        return executeSparqlQueryForEntity(semEntity, query);
    }
}
