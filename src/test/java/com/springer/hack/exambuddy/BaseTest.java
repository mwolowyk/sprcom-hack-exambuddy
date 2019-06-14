package com.springer.hack.exambuddy;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@ComponentScan(basePackageClasses = BaseTest.class)
@DataNeo4jTest
@RunWith(SpringRunner.class)
public abstract class BaseTest {
    @Autowired
    protected EntityFactory entityFactory;
}
