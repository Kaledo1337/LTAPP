package org.example.controllers;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * The WebController class handles web requests related to document signing and file uploads.
 */
@Tag(name = "Signing", description = "Tutorial management sign documents")
@RestController
public class WebController {


    /**
     * Uploads a file to the server.
     *
     * @param file The file to be uploaded.
     * @return The HTTP response containing the uploaded file.
     */
    @Timed("testSIGNDOC")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(), mediaType = "multipart/form-data")})})
    @RequestMapping(value = "/api/signDoc", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity signDocument(@RequestParam("file") MultipartFile file) {
        try {
            // Convert MultipartFile to InputStreamResource
            InputStreamResource resource = new InputStreamResource(file.getInputStream());

            // Prepare ResponseEntity with file as resource and HTTP headers
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getOriginalFilename() + "\"")
                    .contentLength(file.getSize())
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
}
