package com.ryabokon.search.api;

import com.ryabokon.search.api.exception.NotFoundException;
import com.ryabokon.search.api.model.DocumentItem;
import com.ryabokon.search.data.Storage;
import com.ryabokon.search.data.model.Document;
import com.ryabokon.search.data.model.DocumentInfo;
import com.ryabokon.search.index.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final Index searchIndex;
    private final Storage storage;

    @Autowired
    public DocumentService(Index searchIndex, Storage storage) {
        this.searchIndex = searchIndex;
        this.storage = storage;
    }

    public void addDocument(String documentName, String content) {
        Document doc = new Document(content);
        storage.addDocument(documentName, doc);
        searchIndex.addDocument(documentName, content);
    }

    public DocumentItem getDocumentContent(String documentName) {
        Document doc = storage.getDocument(documentName);
        if (doc == null) {
            throw new NotFoundException();
        }
        return new DocumentItem(documentName, doc.getContent(), null);
    }

    public List<DocumentItem> getDocumentsByWords(List<String> words) {
        List<DocumentInfo> docs = searchIndex.findDocumentsByWords(words);
        return mapToSearchResults(docs);

    }

    private List<DocumentItem> mapToSearchResults(List<DocumentInfo> docs) {
        return docs.stream()
                .map(doc ->
                        new DocumentItem(
                                doc.getDocumentName(),
                                storage.getDocument(doc.getDocumentName()).getContent(),
                                doc.getRank()
                        )
                )
                .collect(Collectors.toList());
    }


}
