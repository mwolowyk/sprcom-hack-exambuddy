package com.springer.hack.exambuddy.sementity;

import com.springer.hack.exambuddy.external.dbpediaspotlight.DBPediaSpotlightService;
import com.springer.hack.exambuddy.pdf.PDFEntityExtractor;
import com.springer.hack.exambuddy.pdf.PDFTextExtractor;
import com.springer.hack.exambuddy.pdf.PageWithEntities;
import io.swagger.annotations.ApiParam;
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

    private final PDFEntityExtractor pdfEntityExtractor;

    private Logger log = LoggerFactory.getLogger(SemEntityController.class);


    public SemEntityController(PDFTextExtractor pdfTextExtractor, DBPediaSpotlightService dbPediaSpotlightService, PDFEntityExtractor pdfEntityExtractor) {
        this.pdfTextExtractor = pdfTextExtractor;
        this.dbPediaSpotlightService = dbPediaSpotlightService;
        this.pdfEntityExtractor = pdfEntityExtractor;
    }

    @RequestMapping(value = "text/{text}", method = RequestMethod.GET)
    @ResponseBody
    public List<SemEntity> getSemEntities(@PathVariable("text") String contentString, @ApiParam(required = true, allowableValues = "de,en")  @RequestParam(required = true) String language) {
        List<SemEntity> semEntities = dbPediaSpotlightService.extractSemEntities(contentString, DBPediaSpotlightService.Language.valueOf(language)).join();
        log.info("Getting sementities for string:  {}", contentString);

        semEntities.forEach(semEntity -> System.out.println(semEntity.getSurfaceForm() + " =>  " + semEntity.getUri()));
        return semEntities;
    }

    @RequestMapping(value = "file", method = RequestMethod.POST)
    @ResponseBody
    public List<PageWithEntities> getSemEntitiesFromFile(@RequestPart final MultipartFile file,
                                                         @ApiParam(required = true, allowableValues = "de,en") @RequestParam(required = true) String language,
                                                         @RequestParam(required = false) Integer fromPage,
                                                         @RequestParam(required = false) Integer toPage
    ) {
        List<PageWithEntities> result = new ArrayList<>();
        try {
            DBPediaSpotlightService.Language lang = DBPediaSpotlightService.Language.valueOf(language);
            System.out.println("Got language: " + lang);
            result = pdfEntityExtractor.extractEntities(file.getInputStream(), fromPage, toPage, lang);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
