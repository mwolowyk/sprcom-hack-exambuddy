package com.springer.hack.exambuddy.sementity;

import com.springer.hack.exambuddy.external.dbpediaspotlight.DBPediaSpotlightService;
import com.springer.hack.exambuddy.pdf.PDFTextExtractor;
import com.springer.hack.exambuddy.pdf.PageWithEntities;
import com.springer.hack.exambuddy.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/sementities")
public class SemEntityController {

    private final PDFTextExtractor pdfTextExtractor;

    private final DBPediaSpotlightService dbPediaSpotlightService;

    private Logger log = LoggerFactory.getLogger(SemEntityController.class);


    public SemEntityController(PDFTextExtractor pdfTextExtractor, DBPediaSpotlightService dbPediaSpotlightService) {
        this.pdfTextExtractor = pdfTextExtractor;
        this.dbPediaSpotlightService = dbPediaSpotlightService;
    }

    @RequestMapping(value = "text/{text}", method = RequestMethod.GET)
    @ResponseBody
    public List<SemEntity> getSemEntities(@PathVariable("text") String contentString) {
        List<SemEntity> semEntities = dbPediaSpotlightService.extractSemEntities(contentString).join();
        log.info("Getting sementities for string:  {}", contentString);

        semEntities.forEach(semEntity -> System.out.println(semEntity.getSurfaceForm() + " =>  " + semEntity.getUri()));
        return semEntities;
    }

    @RequestMapping(value = "file", method = RequestMethod.POST)
    @ResponseBody
    public List<PageWithEntities> getSemEntitiesFromFile(@RequestPart final MultipartFile file,
                                                         @RequestParam(required = false) int fromPage,
                                                         @RequestParam(required = false) int toPage) {
        List<PageWithEntities> result = new ArrayList<>();
        List<String> content = null;
        try {
            content = pdfTextExtractor.extractText(file.getInputStream());
            int pageNr = fromPage;
            for(String contentString : content){
                contentString = Utils.normalizeString(contentString);
                System.out.println("<<<<< PAGE >>>>> \n " + '<' + contentString + '>');
                PageWithEntities pageWithEntities = new PageWithEntities(contentString, pageNr);
                try{
                    List<SemEntity> semEntities = dbPediaSpotlightService.extractSemEntities(contentString).join();
                    System.out.println("For string  " + contentString+ "\n ---- Entities: ----- for string ");
                    pageWithEntities.setSemEntities(semEntities);
                    semEntities.forEach(semEntity -> System.out.println(semEntity.getSurfaceForm() + " =>  " + semEntity.getUri()));
                    result.add(pageWithEntities);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                pageNr++;
                if(pageNr > toPage){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}
