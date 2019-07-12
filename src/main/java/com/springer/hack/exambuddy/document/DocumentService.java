package com.springer.hack.exambuddy.document;

import com.springer.hack.exambuddy.external.dbpediaspotlight.DBPediaSpotlightService;
import com.springer.hack.exambuddy.page.Page;
import com.springer.hack.exambuddy.pdf.PDFEntityExtractor;
import com.springer.hack.exambuddy.pdf.PDFManager;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepo documentRepo;

    private final PDFManager pdfManager;

    private final PDFEntityExtractor pdfEntityExtractor;

    public DocumentService(DocumentRepo documentRepo, PDFManager pdfManager, PDFEntityExtractor pdfEntityExtractor) {
        this.documentRepo = documentRepo;
        this.pdfManager = pdfManager;
        this.pdfEntityExtractor = pdfEntityExtractor;
    }


    @Transactional
    public Document save(Document document) {
        return documentRepo.save(document);
    }

    public Document findByTitle(String documentTitle) {
        return documentRepo.findByTitle(documentTitle);
    }

    public Document analyseDocument(MultipartFile file, DBPediaSpotlightService.Language lang, String documentType, Integer fromPage, Integer toPage) throws IOException {
        String documentTitle = file.getOriginalFilename();

        Document document = this.findByTitle(documentTitle);


        PDDocument pdfDocument = pdfManager.loadDocument(file.getInputStream());
        PDDocumentInformation info = pdfDocument.getDocumentInformation();
        if (document == null) {
            switch (documentType) {
                case "script":
                    document = new ScriptDocument(documentTitle);
                    break;
                case "learnMaterial":
                    document = new LearnMaterialDocument(documentTitle);
                    break;
            }
        }

        if (document != null) {
            document.setMetadata(info);

            List<String> pageContents = pdfManager.extractText(pdfDocument);
            List<Page> pages = pdfEntityExtractor.extractEntities(document, pageContents, lang, fromPage, toPage);
            document.setPages(pages);
            document = this.save(document);
        }

        pdfDocument.close();


        return document;
    }
}
