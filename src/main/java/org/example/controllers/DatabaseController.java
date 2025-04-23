package org.example.controllers;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.database.entity.DocEntity;
import org.example.database.repository.DocsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * DatabaseController class is responsible for managing requests to the tutorial management database.
 */
@Tag(name = "Database", description = "Tutorial management database requests")
@RestController
public class DatabaseController {
    /**
     * The DocsRepository variable represents a repository for storing and retrieving documents.
     */
    private final DocsRepository docsRepository;

    /**
     * Constructs a new DatabaseController object.
     *
     * @param docsRepository the DocsRepository instance for storing and retrieving documents
     */
    @Autowired
    public DatabaseController(DocsRepository docsRepository) {
        this.docsRepository = docsRepository;
    }


    /**
     * Performs a search by document name in the database.
     *
     * @param name the name of the document to search for
     * @return an iterable collection of DocEntity objects matching the specified name
     */
    @Timed("getDocByName")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(), mediaType = "application/json")})})
    @GetMapping(path = "/api/getDocByName")
    @Transactional
    public @ResponseBody Iterable<DocEntity> getDocsByName(@RequestParam(value = "name") String name) {
        return docsRepository.findByName(name);
    }

    /**
     * Uploads a file to the database.
     *
     * @param file the file to be uploaded
     * @return a ResponseEntity object indicating the status of the upload process
     */
    @Timed("addDoc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(), mediaType = "multipart/form-data")})})
    @RequestMapping(value = "/api/addDoc", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            DocEntity doc = new DocEntity();
            doc.setName(file.getOriginalFilename());
            doc.setDocument(file.getBytes());
            docsRepository.save(doc);
            return new ResponseEntity("File uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
