package com.ryabokon.search;

import com.ryabokon.search.data.model.DocumentInfo;
import com.ryabokon.search.index.Index;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchIndexTest {

    private static Boolean dataIsLoaded = false; //@Autowired doesn't work for static index in static BeforeClass

    @Autowired
    private Index index;

    @Before
    public void init() throws IOException {

        if (!dataIsLoaded) {

            String java = IOUtils.toString(SearchIndexTest.class.getClassLoader().getResourceAsStream("java.txt"), "UTF-8");
            String history = IOUtils.toString(SearchIndexTest.class.getClassLoader().getResourceAsStream("history.txt"), "UTF-8");
            String principles = IOUtils.toString(SearchIndexTest.class.getClassLoader().getResourceAsStream("principles.txt"), "UTF-8");

            index.addDocument("java", java);
            index.addDocument("history", history);
            index.addDocument("principles", principles);

            dataIsLoaded = true;
        }
    }


    @Test
    public void singleWordSearchTest() {
        List<DocumentInfo> found = index.findDocumentsByWords(Arrays.asList("performance"));

        System.out.println(found.toString());
        Assert.assertNotNull("Search result was null", found);
        Assert.assertEquals("'performance' word must be found exactly once", 1, found.size());
        Assert.assertEquals("'performance' word must be found in 'principles' doc", "principles", found.get(0).getDocumentName());
        Assert.assertTrue("principles doc should have rank 1", found.get(0).getRank() == 1);
    }

    @Test
    public void multipleWordsSearchTest() {
        List<DocumentInfo> found = index.findDocumentsByWords(Arrays.asList("performance", "oriented"));
        //Performance is also present in 'java' doc

        Assert.assertEquals("'performance oriented' words must be found once", 1, found.size());
        Assert.assertEquals("'performance oriented' word must be found in 'principles' doc", "principles", found.get(0).getDocumentName());
        Assert.assertTrue("principles doc should have rank 2", found.get(0).getRank() == 2);
    }

    @Test
    public void caseInsensitiveSearchTest() {
        List<DocumentInfo> caps = index.findDocumentsByWords(Arrays.asList("SUN"));
        List<DocumentInfo> lowercase = index.findDocumentsByWords(Arrays.asList("sun"));

        Assert.assertTrue("Results for uppercase and lowercase search should match", caps.size() == lowercase.size());
    }

    @Test
    public void numericSearchTest() {
        List<DocumentInfo> found = index.findDocumentsByWords(Arrays.asList("Java", "9"));
        Assert.assertNotNull("Search result was null", found);
        Assert.assertTrue("word must be found exactly two times", found.size() == 2);
        Assert.assertTrue("history doc should have rank 28", found.get(0).getRank() == 28);
    }

    @Test
    public void missingWordSearchTest() {
        List<DocumentInfo> found = index.findDocumentsByWords(Arrays.asList("guava"));
        Assert.assertNotNull("Search result was null", found);
        Assert.assertTrue("Search result should be empty", found.isEmpty());
    }

    @Test
    public void emptySearchTest() {
        List<DocumentInfo> found = index.findDocumentsByWords(Arrays.asList(""));
        Assert.assertNotNull("Search result was null", found);
        Assert.assertTrue("Search result should be empty", found.isEmpty());
    }
}
