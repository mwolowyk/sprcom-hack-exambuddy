package com.springer.hack.exambuddy.sementity;


import com.springer.hack.exambuddy.external.dbpedia.DBPediaSparqlService;
import com.springer.hack.exambuddy.external.dbpediaspotlight.DBPediaSpotlightService;
import com.springer.hack.exambuddy.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SemEntityService {

    private static final Double MIN_SIMILARITY = 0.02;
    private final SemEntityRepository semEntityRepository;

    private final DBPediaSpotlightService dbPediaSpotlightService;
    private final DBPediaSparqlService dbPediaSparqlService;

    private Logger log = LoggerFactory.getLogger(SemEntityService.class);


    @Autowired
    public SemEntityService(SemEntityRepository semEntityRepository, DBPediaSpotlightService dbPediaSpotlightService, DBPediaSparqlService dbPediaSparqlService) {
        this.semEntityRepository = semEntityRepository;
        this.dbPediaSpotlightService = dbPediaSpotlightService;
        this.dbPediaSparqlService = dbPediaSparqlService;
    }

    void createSemEntityMapping(Map<String, Object> rowData, SemEntity.SemEntityType semEntityType) {
        SemEntity srcEntity = findOrCreateSemEntity(String.valueOf(rowData.get("from")), semEntityType);
        SemEntity trgtEntity = findOrCreateSemEntity(String.valueOf(rowData.get("to")), semEntityType);
        SemEntityRelation semEntityRelation = new SemEntityRelation(srcEntity, trgtEntity);
        semEntityRelation.setSimilarity(Double.valueOf(String.valueOf(rowData.get("sim"))));
        srcEntity.getRelatedEntities().add(semEntityRelation);
        semEntityRepository.save(srcEntity);
    }

    public SemEntity findOrCreateSemEntity(String uri, SemEntity.SemEntityType semEntityType) {
        return semEntityRepository.findOneByUriAndType(uri, semEntityType).orElseGet(() -> createSemEntity(uri, semEntityType));
    }

    private SemEntity createSemEntity(String uri, SemEntity.SemEntityType semEntityType) {
        String stringValue = uri.trim().replace("http://dbpedia.org/resource/", "dbo:");
        SemEntity semEntity = new SemEntity();
        semEntity.setType(semEntityType);
        semEntity.setUri(uri);
        semEntity.setValue(stringValue);
        semEntity.setSurfaceForm(stringValue);
        return semEntityRepository.save(semEntity);
    }

    public void calculateRelatedness() {
        List<SemEntity> semEntities = semEntityRepository.findAllByType(SemEntity.SemEntityType.DBPEDIA);

        Map<SemEntity, List<String>> wikipageLinksMap = getWikiPageLinks(semEntities);

        calculateSimilarity(wikipageLinksMap);
    }

    private void calculateSimilarity(Map<SemEntity, List<String>> wikipageLinksMap) {
        for (SemEntity semEntity : wikipageLinksMap.keySet()) {
            for (SemEntity semEntity2 : wikipageLinksMap.keySet()) {
                if (semEntity.getValue().compareTo(semEntity2.getValue()) > 0) {
                    List<String> wikipageLinks = wikipageLinksMap.get(semEntity);
                    List<String> wikipageLinks2 = wikipageLinksMap.get(semEntity2);
                    Collection<String> intersection = CollectionUtils.getIntersection(wikipageLinks, wikipageLinks2);
                    Double similarity = CollectionUtils.calculateSimilarity(wikipageLinks, wikipageLinks2, CollectionUtils.SimilarityCalculationType.dice);

                    if(similarity > MIN_SIMILARITY){
                        SemEntityRelation semEntityRelation = new SemEntityRelation(semEntity, semEntity2);
                        semEntityRelation.setSimilarity(similarity);
                        semEntityRelation.setCommonLinks(String.join(",", intersection));
                        semEntity.getRelatedEntities().add(semEntityRelation);

                        log.info("{} -> {} = {}", semEntity.getValue(), semEntity2.getValue(), similarity);
                        semEntityRepository.save(semEntity);
                    }

                }
            }
        }
    }

    private Map<SemEntity, List<String>> getWikiPageLinks(List<SemEntity> semEntities) {
        Map<SemEntity, List<String>> wikiPageLinksMap = new HashMap<>();
        for (SemEntity semEntity : semEntities) {
            List<String> wikiPageLinks = dbPediaSparqlService.fetchWikipageLinks(semEntity, 0).join();
            wikiPageLinksMap.put(semEntity, wikiPageLinks);
        }
        return wikiPageLinksMap;
    }
}
