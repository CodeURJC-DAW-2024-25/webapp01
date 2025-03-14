package es.daw01.savex.DTOs.posts;

import java.util.List;

import es.daw01.savex.model.VisibilityType;

public record CreatePostRequest(
    String title,
    String description,
    String content,
    String author,
    String date,
    List<String> tags,
    VisibilityType visibility
) {}
