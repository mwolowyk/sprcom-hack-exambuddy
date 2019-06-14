package com.springer.hack.exambuddy.pdf;

import com.springer.hack.exambuddy.BaseTest;
import com.springer.hack.exambuddy.external.dbpediaspotlight.DBPediaSpotlightService;
import com.springer.hack.exambuddy.sementity.SemEntity;
import com.springer.hack.exambuddy.utils.Utils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

public class PDFTextExtractorTest extends BaseTest {

    @Autowired
    PDFTextExtractor pdfTextExtractor;

    @Autowired
    private DBPediaSpotlightService dbPediaSpotlightService;

    @Test
    public void extractText() {
        InputStream pdfFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.pdf");
        List<String> content = pdfTextExtractor.extractText(pdfFileStream);
        content.forEach(s -> System.out.println("<<<<< PAGE >>>>> \n " + '<' + s + '>'));
        assertThat(content.isEmpty(), is(false));
        content.forEach(s -> assertThat(s, not(isEmptyString())));
    }
}