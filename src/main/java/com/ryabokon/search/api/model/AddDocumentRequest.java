package com.ryabokon.search.api.model;

import org.hibernate.validator.constraints.NotBlank;

public class AddDocumentRequest {

    @NotBlank
    private String documentName;
    @NotBlank
    private String content;

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
}
