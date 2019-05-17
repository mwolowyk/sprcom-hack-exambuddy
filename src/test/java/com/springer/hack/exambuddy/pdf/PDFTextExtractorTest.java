package com.springer.hack.exambuddy.pdf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class PDFTextExtractorTest {

    private PDFTextExtractor pdfTextExtractor = new PDFTextExtractor();

    @Test
    public void extractText() {
        InputStream pdfFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.pdf");
        List<String> content = pdfTextExtractor.extractText(pdfFileStream);
        content.forEach(s -> System.out.println("<<<<< PAGE >>>>> \n " + '<' + s + '>'));
        assertThat(content.isEmpty(), is(false));
        content.forEach(s -> assertThat(s, not(isEmptyString())));
    }
}