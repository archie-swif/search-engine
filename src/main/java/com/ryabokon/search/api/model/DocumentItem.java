package com.ryabokon.search.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class DocumentItem {

    private String documentName;
    private String content;
    private Long rank;

    public DocumentItem() {
    }

    public DocumentItem(String documentName, String content, Long rank) {
        this.documentName = documentName;
        this.content = content;
        this.rank = rank;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }
}
