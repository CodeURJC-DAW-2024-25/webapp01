package es.daw01.savex.DTOs.posts;

import java.util.List;

import es.daw01.savex.model.VisibilityType;

public record PostDTO(
    long id,
    String title,
    String description,
    String author,
    String date,
    String readingTime,
    VisibilityType visibility,
    List<String> tags
) {}