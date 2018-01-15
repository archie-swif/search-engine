package com.ryabokon.search.data;

import com.ryabokon.search.data.model.Document;
import com.ryabokon.search.data.model.DocumentInfo;

import java.util.List;

public interface Storage {

    public Document getDocument(String documentName);

    public void addDocument(String documentName, Document doc);

    public List<DocumentInfo> getDocumentsInfoByWord(String word);

    public void addDocumentInfo(String word, DocumentInfo docInfo);

    public void cleanUp();
}
