package com.springer.hack.exambuddy.pdf;

import com.springer.hack.exambuddy.sementity.SemEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PageWithEntities {

    int pageNumber;
    String text;
    List<SemEntity> semEntities = new ArrayList<>();

    public PageWithEntities(String text, int pageNumber) {
        this.pageNumber = pageNumber;
        this.text = text;
    }

    public void setSemEntities(List<SemEntity> semEntities) {
        this.semEntities = semEntities;
    }
}
