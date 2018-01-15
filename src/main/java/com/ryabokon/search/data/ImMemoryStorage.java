package com.ryabokon.search.data;

import com.ryabokon.search.data.model.Document;
import com.ryabokon.search.data.model.DocumentInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository()
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ImMemoryStorage implements Storage {

    //Reverse index of the documents
    //Stores values like: word -> [(readme.txt, 3), (doc 2 name)
    private Map<String, List<DocumentInfo>> index = new ConcurrentHashMap<>();

    //Stores document contents by doc name
    //Documents can be stored separately, in a document DB
    private Map<String, Document> documents = new ConcurrentHashMap<>();

    @Override
    public Document getDocument(String documentName) {
        return documents.get(documentName);
    }

    @Override
    public void addDocument(String documentName, Document doc) {
        documents.put(documentName, doc);
    }

    @Override
    public List<DocumentInfo> getDocumentsInfoByWord(String word) {
        return index.getOrDefault(word, Collections.emptyList());
    }

    @Override
    public void addDocumentInfo(String word, DocumentInfo docInfo) {
        List<DocumentInfo> docs = index.computeIfAbsent(word, key -> new ArrayList<>());
        docs.add(docInfo);
        index.put(word, docs);
    }

    @Override
    public void cleanUp() {
        index.clear();
        documents.clear();
    }
}
