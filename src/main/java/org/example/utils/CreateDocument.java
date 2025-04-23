package org.example.utils;

import jakarta.xml.bind.annotation.XmlRootElement;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * The CreateDocument class represents a document that can be created and converted to a Blob.
 */
@XmlRootElement
public class CreateDocument {

    /**
     * Represents the content of a document.
     *
     * This class provides methods for retrieving and setting the content of the document,
     * as well as converting the content to a Blob object.
     */
    private String content;

    /**
     * The CreateDocument class represents a document that can be created and converted to a Blob.
     *
     * This class provides methods for retrieving and setting the content of the document,
     * as well as converting the content to a Blob object.
     */
    public CreateDocument() {
    }

    /**
     * Constructs a new CreateDocument object.
     *
     * @param content The content of the document as a String.
     */
    public CreateDocument(String content) {
        this.content = content;
    }

    /**
     * Retrieves the content of the document.
     *
     * @return The content of the document as a String.
     */
    public String getContent() {
        return content;
    }

    /**
     * Setter for the content of the document.
     *
     * @param content The new content of the document.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Converts the content of the document to a Blob object.
     *
     * @return A Blob object representing the content of the document.
     * @throws RuntimeException if an error occurs while transforming the document to Blob.
     */
    public Blob toBlob() {
        try {
            byte[] contentBytes = content.getBytes();
            Blob blob = new SerialBlob(contentBytes);
            return blob;
        } catch (SQLException e) {
            throw new RuntimeException("Error while transforming document to Blob", e);
        }
    }
}
