package com.springer.hack.exambuddy.integration;

import com.springer.hack.exambuddy.BaseTest;
import com.springer.hack.exambuddy.external.dbpediaspotlight.DBPediaSpotlightService;
import com.springer.hack.exambuddy.pdf.PDFManager;
import com.springer.hack.exambuddy.sementity.SemEntity;
import com.springer.hack.exambuddy.utils.Utils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.List;

public class IntegrationTest extends BaseTest {

    @Autowired
    PDFManager pdfManager;

    @Autowired
    private DBPediaSpotlightService dbPediaSpotlightService;

    @Test
    @Ignore
    public void getEntitiesFromPdf() {
        InputStream pdfFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.pdf");
        List<String> content = pdfManager.extractText(pdfFileStream);
        for(String contentString : content){
            contentString = Utils.normalizeString(contentString);
            System.out.println("<<<<< PAGE >>>>> \n " + '<' + contentString + '>');
            try{
                List<SemEntity> semEntities = dbPediaSpotlightService.extractSemEntities(contentString, DBPediaSpotlightService.Language.de).join();
                System.out.println("For string  " + contentString+ "\n ---- Entities: ----- for string ");
                semEntities.forEach(semEntity -> System.out.println(semEntity.getSurfaceForm() + " =>  " + semEntity.getUri()));
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }
}