package com.springer.hack.exambuddy;


import com.springer.hack.exambuddy.sementity.SemEntity;
import com.springer.hack.exambuddy.sementity.SemEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityFactory {

    @Autowired
    private SemEntityService semEntityService;

    public SemEntity createSemEntity(String uri, SemEntity.SemEntityType type) {
        return semEntityService.findOrCreateSemEntity(uri, type);
    }
}
