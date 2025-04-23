package org.example.database.entity;


import jakarta.persistence.*;

@Entity
public class DocEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public void setName(String name) {
        this.name = name;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public byte[] getDocument() {
        return document;
    }

    private String name;
    @Lob
    private byte[] document;

    public DocEntity() {
    }

    public DocEntity(String name, byte[] document) {
        this.name = name;
        this.document = document;
    }

}