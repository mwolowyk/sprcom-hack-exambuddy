package com.springer.hack.exambuddy.pdf;

import com.springer.hack.exambuddy.BaseTest;
import com.springer.hack.exambuddy.external.dbpediaspotlight.DBPediaSpotlightService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

public class PDFManagerTest extends BaseTest {

    @Autowired
    PDFManager pdfManager;

    @Autowired
    private DBPediaSpotlightService dbPediaSpotlightService;

    @Test
    public void extractText() {
        InputStream pdfFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.pdf");
        List<String> content = pdfManager.extractText(pdfFileStream);
        content.forEach(s -> System.out.println("<<<<< PAGE >>>>> \n " + '<' + s + '>'));
        assertThat(content.isEmpty(), is(false));
        content.forEach(s -> assertThat(s, not(isEmptyString())));
    }
}