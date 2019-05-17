package com.springer.hack.exambuddy.pdf;


import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PDFTextExtractor {

    public List<String> extractText(InputStream inputStream) {
        List<String> pdfPageContent = new ArrayList<>();
        PDDocument document = new PDDocument();
        try {
            document = PDDocument.load(inputStream);
            List<PDDocument> pages = new Splitter().split(document);
            pdfPageContent = pages.stream()
                    .map(this::extractText)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            document.close();
        } catch (IOException e) {
            try {
                document.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return pdfPageContent;
    }

    private String extractText(PDDocument page) {
        try {
            return new PDFTextStripper().getText(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
