package com.springer.hack.exambuddy.pdf;

import com.springer.hack.exambuddy.external.dbpediaspotlight.DBPediaSpotlightService;
import com.springer.hack.exambuddy.sementity.SemEntity;
import com.springer.hack.exambuddy.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PDFEntityExtractor {

    private final PDFTextExtractor pdfTextExtractor;

    private final DBPediaSpotlightService dbPediaSpotlightService;

    public PDFEntityExtractor(PDFTextExtractor pdfTextExtractor, DBPediaSpotlightService dbPediaSpotlightService) {
        this.pdfTextExtractor = pdfTextExtractor;
        this.dbPediaSpotlightService = dbPediaSpotlightService;
    }

    public List<PageWithEntities> extractEntities(InputStream inputStream, int fromPage, int toPage) {
        List<PageWithEntities> result = new ArrayList<>();
        List<String> content = null;
        content = pdfTextExtractor.extractText(inputStream);
        int pageNr = 1;
        for (String contentString : content) {
            if(pageNr < fromPage){
                pageNr++;
                continue;
            }

            contentString = Utils.normalizeString(contentString);
            PageWithEntities pageWithEntities = new PageWithEntities(contentString, pageNr);
            try {
                List<SemEntity> semEntities = dbPediaSpotlightService.extractSemEntities(contentString).join();
                pageWithEntities.setSemEntities(semEntities);
                result.add(pageWithEntities);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            pageNr++;

            if (pageNr > toPage) {
                break;
            }
        }

        return result;
    }

}
