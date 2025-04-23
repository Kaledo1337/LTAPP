package org.example.swagger;

/**
 * The Tutorial class represents a tutorial entity.
 */
public record Tutorial(
        long id,
        String title,
        String description,
        boolean isPublished) {
}