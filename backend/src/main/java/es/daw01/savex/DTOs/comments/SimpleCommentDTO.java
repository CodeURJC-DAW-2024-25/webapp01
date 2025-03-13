package es.daw01.savex.DTOs.comments;

public record SimpleCommentDTO(
    Long id,
    String content,
    String formatedDate,
    String author
) {}
