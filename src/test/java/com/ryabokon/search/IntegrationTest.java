package com.ryabokon.search;

import com.ryabokon.search.api.model.AddDocumentRequest;
import com.ryabokon.search.api.model.DocumentItem;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {


    private static final String GET_DOCUMENT_URL = "/documents/{documentName}";
    private static final String GET_DOCUMENT_PARAM = "documentName";
    private static final String ADD_DOCUMENT_URL = "/documents";
    private static final String SEARCH_DOCUMENT_URL = "/search/documents";

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void simpleDocTest() {

        String key = "document-key";
        String content = "Some sample content here.";

        //---------------Add document-------------------


        AddDocumentRequest request = new AddDocumentRequest();
        request.setDocumentName(key);
        request.setContent(content);

        with()
                .contentType("application/json")
                .body(Arrays.asList(request))
                .post(ADD_DOCUMENT_URL)
                .then()
                .statusCode(201);

        //---------------Get document-------------------


        with()
                .contentType("application/json")
                .pathParam(GET_DOCUMENT_PARAM, key)
                .get(GET_DOCUMENT_URL)
                .then()
                .statusCode(200)
                .body("documentName", is(key))
                .body("content", is(content));

        //---------------Search-------------------

        List<String> searchWords = Arrays.asList("sample");

        with()
                .contentType("application/json")
                .param("words", searchWords)
                .get(SEARCH_DOCUMENT_URL)
                .then()
                .statusCode(200)
                .body("[0].documentName", is(key))
                .body("[0].content", is(content))
                .body("[0].rank", is(1));

        //---------------Cleanup-------------------

        with()
                .contentType("application/json")
                .delete(SEARCH_DOCUMENT_URL)
                .then()
                .statusCode(200);

        //---------------Not found-------------------

        with()
                .contentType("application/json")
                .pathParam(GET_DOCUMENT_PARAM, key)
                .get(GET_DOCUMENT_URL)
                .then()
                .statusCode(404);
    }


    @Test
    public void aLotOfDocsTest() throws IOException, URISyntaxException {

        List<AddDocumentRequest> requests = Files.find(Paths.get(this.getClass().getClassLoader().getResource(".").toURI()), 1, (p, a) -> p.toString().toLowerCase().endsWith(".txt"))
                .map(path -> {
                    try {
                        String filename = path.getFileName().toString();
                        String content = FileUtils.readFileToString(path.toFile());
                        AddDocumentRequest request = new AddDocumentRequest();
                        request.setDocumentName(filename);
                        request.setContent(content);
                        return request;
                    } catch (Exception e) {
                    }
                    return null;
                }).collect(Collectors.toList());

        with()
                .contentType("application/json")
                .body(requests)
                .post(ADD_DOCUMENT_URL)
                .then()
                .statusCode(201);

        List<String> searchWords = Arrays.asList("java", "libraries");

        ResponseBody response = with()
                .contentType("application/json")
                .param("words", searchWords)
                .get(SEARCH_DOCUMENT_URL)
                .then()
                .statusCode(200).extract().response().getBody();

        List<DocumentItem> responseItems = Arrays.asList(response.as(DocumentItem[].class));

        Assert.assertEquals(2, responseItems.size());
        Assert.assertEquals("java.txt", responseItems.get(0).getDocumentName());
        Assert.assertTrue(responseItems.get(0).getRank() == 19L);
        Assert.assertEquals("platform.txt", responseItems.get(1).getDocumentName());
        Assert.assertTrue(responseItems.get(1).getRank() == 13L);
    }

}
