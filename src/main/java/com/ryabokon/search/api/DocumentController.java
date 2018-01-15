package com.ryabokon.search.api;

import com.ryabokon.search.api.model.AddDocumentRequest;
import com.ryabokon.search.api.model.DocumentItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@Api(value = "Document API", description = "Add or search documents")
@Validated
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @RequestMapping(value = "/search/documents", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("Search on a string of tokens to return keys of all documents that contain all tokens in the set")
    public List<DocumentItem> searchByWord(@RequestParam @Valid @NotEmpty List<String> words) {
        return documentService.getDocumentsByWords(words);
    }

    @RequestMapping(value = "/documents", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Put documents into the search engine by key")
    public void addDocument(@RequestBody @Valid @NotEmpty List<AddDocumentRequest> addDocumentRequest) {
        addDocumentRequest.forEach(doc ->
                documentService.addDocument(doc.getDocumentName(), doc.getContent())
        );
    }

    @RequestMapping(value = "/documents/{documentName}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("Get document by key")
    public DocumentItem getDocumentById(@PathVariable @Valid @NotBlank String documentName) {
        return documentService.getDocumentContent(documentName);
    }

    @RequestMapping(value = "/search/documents", method = RequestMethod.DELETE)
    @ResponseBody
    @ApiOperation("Clear the search index and document store")
    public void cleanUp() {
        documentService.cleanUp();
    }

    @ApiIgnore
    @RequestMapping("/")
    public void home(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }
}
