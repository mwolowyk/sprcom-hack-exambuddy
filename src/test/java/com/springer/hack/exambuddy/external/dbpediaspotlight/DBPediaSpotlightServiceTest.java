package com.springer.hack.exambuddy.external.dbpediaspotlight;


import com.springer.hack.exambuddy.BaseTest;
import com.springer.hack.exambuddy.sementity.SemEntity;
import com.springer.hack.exambuddy.sementity.SemEntityRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DBPediaSpotlightServiceTest extends BaseTest {

    @Autowired
    private DBPediaSpotlightService dbPediaSpotlightService;

    @Autowired
    private SemEntityRepository semEntityRepository;

    @Test
    public void extractSemanticEntities() {
        String value = "Cotton is a soft, fluffy staple fiber that grows in a boll, or protective case, around the seeds of the cotton plants of the genus Gossypium in the mallow family Malvaceae. The fiber is almost pure cellulose. Under natural conditions, the cotton bolls will increase the dispersal of the seeds.";
        List<SemEntity> semEntities = dbPediaSpotlightService.extractSemEntities(value).join();
        assertThat(semEntities).isNotEmpty();

        List<SemEntity> foundSemEntities = semEntityRepository.findAllByType(SemEntity.SemEntityType.DBPEDIA);
        semEntities.forEach(semEntity -> {
            assertThat(semEntity.getUri()).isNotEmpty();
            assertThat(semEntity.getValue()).isNotEmpty();
            assertThat(semEntity.getConfidence()).isNotZero();
            assertThat(foundSemEntities.contains(semEntity)).isTrue();
        });
    }
}
