package com.springer.hack.exambuddy.sementity;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemEntityRepository extends Neo4jRepository<SemEntity, Long> {
    Optional<SemEntity> findOneByValueAndType(String value, SemEntity.SemEntityType type);
    Optional<SemEntity> findOneByUriAndType(String uri, SemEntity.SemEntityType type);

    List<SemEntity> findAllByType(SemEntity.SemEntityType type);
}
