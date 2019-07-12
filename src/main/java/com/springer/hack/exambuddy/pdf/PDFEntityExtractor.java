package com.springer.hack.exambuddy.pdf;

import com.springer.hack.exambuddy.document.Document;
import com.springer.hack.exambuddy.external.dbpediaspotlight.DBPediaSpotlightService;
import com.springer.hack.exambuddy.page.Page;
import com.springer.hack.exambuddy.sementity.SemEntity;
import com.springer.hack.exambuddy.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PDFEntityExtractor {

    private final PDFManager pdfManager;

    private final DBPediaSpotlightService dbPediaSpotlightService;

    private Logger log = LoggerFactory.getLogger(PDFEntityExtractor.class);


    public PDFEntityExtractor(PDFManager pdfManager, DBPediaSpotlightService dbPediaSpotlightService) {
        this.pdfManager = pdfManager;
        this.dbPediaSpotlightService = dbPediaSpotlightService;
    }

    public List<Page> extractEntities(InputStream inputStream, Integer fromPage, Integer toPage, DBPediaSpotlightService.Language language) {
        List<String> content = pdfManager.extractText(inputStream);
        return extractEntities(null, content, language, fromPage, toPage);
    }

    public List<Page> extractEntities(Document document, List<String> pageContents, DBPediaSpotlightService.Language language, Integer fromPage, Integer toPage){
        List<Page> result = new ArrayList<>();
        int pageNr = fromPage;
        if(toPage != null){
            pageContents = pageContents.subList(fromPage-1, toPage);
        }

        for(String contentString: pageContents ){
            contentString = Utils.normalizeString(contentString);
            Optional<Page> foundPageOpt = document.getPageByPageNumber(pageNr);

            if(foundPageOpt.isPresent() && foundPageOpt.get().getText().equals(contentString)){
                log.info("Skipping already analysed page {}.", pageNr);
                Page foundPage = foundPageOpt.get();

                result.add(foundPage);
            }
            else{
                Page pageWithEntities = new Page(pageNr, contentString);
                log.info("Processing page {} with content {}", pageNr, contentString);
                try {
                    List<SemEntity> semEntities = dbPediaSpotlightService.extractSemEntities(contentString, language).join();
                    if(semEntities.isEmpty()){
                        pageWithEntities.setSemEntities(semEntities);
                        result.add(pageWithEntities);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            pageNr++;
        }
        return result;

    }

}
