package com.springer.hack.exambuddy.page;

import com.springer.hack.exambuddy.common.BaseEntity;
import com.springer.hack.exambuddy.semcategory.SemCategory;
import com.springer.hack.exambuddy.sementity.SemEntity;
import lombok.*;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NodeEntity("Page")
@Getter
@Setter
@RequiredArgsConstructor
public class Page extends BaseEntity {

    @NonNull
    int pageNumber;

    @NonNull
    String text;

    @Relationship(type = "HAS_ENTITY")
    private List<SemEntity> semEntities = new ArrayList<>();
}
