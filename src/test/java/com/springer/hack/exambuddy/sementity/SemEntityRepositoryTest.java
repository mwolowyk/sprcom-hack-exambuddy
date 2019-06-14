package com.springer.hack.exambuddy.sementity;


import com.springer.hack.exambuddy.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;



public class SemEntityRepositoryTest extends BaseTest {

    @Autowired
    private SemEntityRepository semEntityRepository;

    @Test
    public void testFindByValue() {
        String value = "TEST_ENTITY";
        SemEntity.SemEntityType type = SemEntity.SemEntityType.CUSTOM;
        SemEntity testSemEntity = entityFactory.createSemEntity(value, type);
        Optional<SemEntity> foundSemEntity = semEntityRepository.findOneByValueAndType(value, type);
        assertThat(foundSemEntity.get()).isEqualTo(testSemEntity);
    }

    @Test
    public void testFindAllByType() {
        SemEntity.SemEntityType type = SemEntity.SemEntityType.CUSTOM;
        List<SemEntity> semEntityList = new ArrayList<>();
        int number = 10;
        for (int i = 0; i < number; i++) {
            String value = "val " + i;
            semEntityList.add(entityFactory.createSemEntity(value, type));
        }

        List<SemEntity> allByType = semEntityRepository.findAllByType(type);

        allByType.forEach(semEntity -> assertThat(semEntity.getType()).isEqualTo(type));
        semEntityList.forEach(semEntity -> assertThat(allByType.contains(semEntity)).isTrue());
    }
}
