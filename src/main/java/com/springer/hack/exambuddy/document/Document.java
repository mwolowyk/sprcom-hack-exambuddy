package com.springer.hack.exambuddy.document;

import com.springer.hack.exambuddy.common.BaseEntity;
import com.springer.hack.exambuddy.page.Page;
import lombok.*;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

@NodeEntity("Document")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public abstract class Document extends BaseEntity {
    @NonNull
    private String title;

    private String author;
    private String keywords;
    private String subject;

    @Relationship(type = "HAS_PAGE")
    private List<Page> pages = new ArrayList<>();

    public void setMetadata(PDDocumentInformation info) {
        this.author = info.getAuthor();
        this.keywords = info.getKeywords();
        this.subject = info.getSubject();
    }

    public Optional<Page> getPageByPageNumber(int pageNumber){
        return this.getPages().stream().filter(page -> page.getPageNumber() == pageNumber).findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;
        Document document = (Document) o;
        return getTitle().equals(document.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }
}
