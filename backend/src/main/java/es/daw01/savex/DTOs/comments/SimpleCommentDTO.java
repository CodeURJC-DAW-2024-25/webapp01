package es.daw01.savex.DTOs.comments;

public record SimpleCommentDTO(
        Long id,
        Long authorId,
        String content,
        String formatedDate,
        String author) {
}
