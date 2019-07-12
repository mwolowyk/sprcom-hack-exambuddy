package com.springer.hack.exambuddy.pdf;


import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PDFManager {

    public PDDocument loadDocument(InputStream inputStream){
        PDDocument document = new PDDocument();
        try {
            document = PDDocument.load(inputStream);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return document;
    }

    public PDDocumentInformation getDocumentMetaData(PDDocument document){
        return document.getDocumentInformation();
    }

    public List<String> extractText(PDDocument document){
        List<String> pdfPageContent = new ArrayList<>();

        List<PDDocument> pages = null;
        try {
            pages = new Splitter().split(document);
            pdfPageContent = pages.stream()
                    .map(this::extractTextFromPage)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfPageContent;
    }

    public List<String> extractText(InputStream inputStream) {
        List<String> pdfPageContent = new ArrayList<>();
        PDDocument document = new PDDocument();
        try {
            document = PDDocument.load(inputStream);
            pdfPageContent = extractText(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                document.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return pdfPageContent;
    }

    private String extractTextFromPage(PDDocument page) {
        try {
            return new PDFTextStripper().getText(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
