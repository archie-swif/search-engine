package com.ryabokon.search.data.model;

public class DocumentInfo {

    private String documentName;
    private Long rank;
    private Integer searchWordsInDoc;

    public DocumentInfo(String documentName, Long rank, Integer searchWordsInDoc) {
        this.documentName = documentName;
        this.rank = rank;
        this.searchWordsInDoc = searchWordsInDoc;
    }

    //Merge two DocumentInfo-s with the same doc name
    public DocumentInfo(DocumentInfo di1, DocumentInfo di2) {
        this.documentName = di1.getDocumentName();
        this.rank = di1.getRank() + di2.getRank();
        this.searchWordsInDoc = di1.getSearchWordsInDoc() + di2.getSearchWordsInDoc();
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Integer getSearchWordsInDoc() {
        return searchWordsInDoc;
    }

    public void setSearchWordsInDoc(Integer searchWordsInDoc) {
        this.searchWordsInDoc = searchWordsInDoc;
    }

    @Override
    public String toString() {
        return "DocumentInfo{" +
                "documentName='" + documentName + '\'' +
                ", rank=" + rank +
                ", searchWordsInDoc=" + searchWordsInDoc +
                '}';
    }
}
