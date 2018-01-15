package com.ryabokon.search.index;

import com.ryabokon.search.data.Storage;
import com.ryabokon.search.data.model.DocumentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReverseIndex implements Index {

    private final Storage storage;

    //Compares two DocumentInfo objects by rank value, biggest first.
    private final Comparator<DocumentInfo> byRank = Comparator.comparingLong(DocumentInfo::getRank).reversed();

    @Autowired
    public ReverseIndex(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void addDocument(String documentName, String content) {
        Map<String, Long> wordsCount =
                Arrays.stream(content.split("\\W+")) // Get words by removing all special chars
                        .map(this::normalize)
                        .collect(                          // Collect to [word -> occurrence]
                                Collectors.groupingBy(Function.identity(), Collectors.counting())
                        );

        // Add to existing index
        wordsCount.forEach((word, count) -> storage.addDocumentInfo(word, new DocumentInfo(documentName, count, 1)));
    }

    @Override
    public List<DocumentInfo> findDocumentsByWords(List<String> words) {
        Integer searchWordsCount = words.size();

        Collection<DocumentInfo> mergedRanks =
                words.stream()
                        .map(this::normalize)
                        .map(storage::getDocumentsInfoByWord)
                        .flatMap(Collection::stream)
                        .collect(
                                // Merge DocumentInfo-s with the same documentName, by adding their rank values
                                // And count how many search words were found, to get docs with ALL words
                                Collectors.toMap(
                                        DocumentInfo::getDocumentName,
                                        Function.identity(),
                                        (di1, di2) -> new DocumentInfo(di1, di2)
                                )
                        )
                        .values();

        //Return only docs that contain all search words
        return mergedRanks.stream()
                .filter(di -> di.getSearchWordsInDoc() >= searchWordsCount)
                .sorted(byRank)
                .collect(Collectors.toList());
    }


    // Normalize search words by converting them to lowercase;
    // Additionally a/the/and words can be skipped.
    // And stemming can be applied: walking, walker -> walk.
    private String normalize(String word) {
        if (word == null) return "";
        return word.toLowerCase();
    }


}
