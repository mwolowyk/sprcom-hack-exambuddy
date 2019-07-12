package com.springer.hack.exambuddy.document;

import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DocumentRepo extends Neo4jRepository<Document, Long> {
    @Depth(value = 2)
    Document findByTitle(String documentTitle);
}
