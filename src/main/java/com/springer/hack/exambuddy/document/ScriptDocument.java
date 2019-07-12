package com.springer.hack.exambuddy.document;

import lombok.RequiredArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity("Script")
public class ScriptDocument extends Document {

    public ScriptDocument(String title){
        super(title);
    }

}
