package com.springer.hack.exambuddy.pdf;

import com.springer.hack.exambuddy.BaseTest;
import com.springer.hack.exambuddy.external.dbpediaspotlight.DBPediaSpotlightService;
import com.springer.hack.exambuddy.page.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PDFEntityExtractorTest extends BaseTest {

    @Autowired
    PDFEntityExtractor pdfEntityExtractor;

    @Test
    public void extractEntities() {
        InputStream pdfFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.pdf");

        List<Page> pageWithEntitiesList = pdfEntityExtractor.extractEntities(pdfFileStream, 3, 4, DBPediaSpotlightService.Language.de);

        pageWithEntitiesList.forEach(pageWithEntities -> {
            System.out.println("<<<<< PAGE " + pageWithEntities.getPageNumber() + " >>>>> \n " + '<' + pageWithEntities.getText() + '>');
            System.out.println("\n ---- Entities: ----- ");
            pageWithEntities.getSemEntities().forEach(semEntity -> System.out.println(semEntity.getSurfaceForm() + " =>  " + semEntity.getUri()));
            assertThat(pageWithEntities.getText()).isNotEmpty();
            assertThat(pageWithEntities.getSemEntities()).isNotEmpty();
        });

    }
}