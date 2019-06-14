package com.springer.hack.exambuddy.pdf;

import com.springer.hack.exambuddy.BaseTest;
import com.springer.hack.exambuddy.sementity.SemEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class PDFEntityExtractorTest extends BaseTest {

    @Autowired
    PDFEntityExtractor pdfEntityExtractor;

    @Test
    public void extractEntities() {
        InputStream pdfFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.pdf");

        List<PageWithEntities> pageWithEntitiesList = pdfEntityExtractor.extractEntities(pdfFileStream, 3, 4);

        pageWithEntitiesList.forEach(pageWithEntities -> {
            System.out.println("<<<<< PAGE " + pageWithEntities.getPageNumber() + " >>>>> \n " + '<' + pageWithEntities.getText() + '>');
            System.out.println("\n ---- Entities: ----- ");
            pageWithEntities.getSemEntities().forEach(semEntity -> System.out.println(semEntity.getSurfaceForm() + " =>  " + semEntity.getUri()));
            assertThat(pageWithEntities.getText()).isNotEmpty();
            assertThat(pageWithEntities.getSemEntities()).isNotEmpty();
        });

    }
}