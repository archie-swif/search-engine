package com.ryabokon.search.index;

import com.ryabokon.search.data.model.DocumentInfo;

import java.util.List;

public interface Index {
    void addDocument(String documentName, String content);

    List<DocumentInfo> findDocumentsByWords(List<String> words);
}
