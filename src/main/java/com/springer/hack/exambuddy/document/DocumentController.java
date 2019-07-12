package com.springer.hack.exambuddy.document;

import com.springer.hack.exambuddy.external.dbpediaspotlight.DBPediaSpotlightService;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("api/v1/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }


    @RequestMapping(value = "file", method = RequestMethod.POST)
    @ResponseBody
    public Document getSemEntitiesFromFile(@RequestPart final MultipartFile file,
                                           @ApiParam(required = true, allowableValues = "de,en") @RequestParam(required = true) String language,
                                           @ApiParam(required = true, allowableValues = "script,learnMaterial") @RequestParam(required = true) String documentType,
                                           @RequestParam(required = false) Integer fromPage,
                                           @RequestParam(required = false) Integer toPage,
                                           HttpServletRequest request, HttpServletResponse response
    ) {
        if (fromPage == null) {
            fromPage = 1;
        }
        DBPediaSpotlightService.Language lang = DBPediaSpotlightService.Language.valueOf(language);
        try {
            return documentService.analyseDocument(file, lang, documentType, fromPage, toPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
