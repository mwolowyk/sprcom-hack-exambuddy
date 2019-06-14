package com.springer.hack.exambuddy.external.dbpedia;


import com.springer.hack.exambuddy.BaseTest;
import com.springer.hack.exambuddy.EntityFactory;
import com.springer.hack.exambuddy.sementity.SemEntity;
import com.springer.hack.exambuddy.sementity.SemEntityRepository;
import com.springer.hack.exambuddy.utils.CollectionUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


public class DBPediaSparqlServiceTest extends BaseTest {

    @Autowired
    private DBPediaSparqlService dbPediaSparqlService;

    @Autowired
    private EntityFactory entityFactory;

    @Autowired
    private SemEntityRepository semEntityRepository;

    @After
    public void tearDown() throws Exception {
        semEntityRepository.deleteAll();
    }

    @Test
    public void calculateSimilarityForSimilarEntities() {
        String uri1 = "http://dbpedia.org/resource/MacBook_Air";
        String uri2 = "http://dbpedia.org/resource/MacBook_Pro";
        SemEntity semEntity1 = entityFactory.createSemEntity(uri1, SemEntity.SemEntityType.DBPEDIA);
        SemEntity semEntity2 = entityFactory.createSemEntity(uri2, SemEntity.SemEntityType.DBPEDIA);

        Double similarity = dbPediaSparqlService.calculateSimilarity(semEntity1, semEntity2, 0, CollectionUtils.SimilarityCalculationType.jaccard);
        System.out.printf("Similarity of %s and %s is %s%n", semEntity1.getValue(), semEntity2.getValue(), similarity);
        assertThat(similarity).isGreaterThan(0);
    }

    @Test
    public void calculateSimilarityForDifferentEntities() {
        String uri1 = "http://dbpedia.org/resource/MacBook_Air";
        String uri2 = "http://dbpedia.org/resource/Berlin";
        SemEntity semEntity1 = entityFactory.createSemEntity(uri1, SemEntity.SemEntityType.DBPEDIA);
        SemEntity semEntity2 = entityFactory.createSemEntity(uri2, SemEntity.SemEntityType.DBPEDIA);

        Double similarity = dbPediaSparqlService.calculateSimilarity(semEntity1, semEntity2, 0, CollectionUtils.SimilarityCalculationType.jaccard);
        System.out.printf("Similarity of %s and %s is %s%n", semEntity1.getValue(), semEntity2.getValue(), similarity);
        assertThat(similarity).isEqualTo(0.0);
    }

    @Test
    public void calculateSimilarityForEntitiesWithoutReferences() {
        String uri1 = "http://dbpedia.org/resource/Left_Front_(West_Bengal)";
        String uri2 = "http://dbpedia.org/resource/Strada_statale_18_Tirrena_Inferiore";
        SemEntity semEntity1 = entityFactory.createSemEntity(uri1, SemEntity.SemEntityType.DBPEDIA);
        SemEntity semEntity2 = entityFactory.createSemEntity(uri2, SemEntity.SemEntityType.DBPEDIA);

        Double similarity = dbPediaSparqlService.calculateSimilarity(semEntity1, semEntity2, 0, CollectionUtils.SimilarityCalculationType.jaccard);
        System.out.printf("Similarity of %s and %s is %s%n", semEntity1.getValue(), semEntity2.getValue(), similarity);
        assertThat(similarity).isEqualTo(0.0);
    }

}