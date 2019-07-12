package com.springer.hack.exambuddy.document;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity("LearnMaterial")
public class LearnMaterialDocument extends Document {
    public LearnMaterialDocument(String title){
        super(title);
    }

}
