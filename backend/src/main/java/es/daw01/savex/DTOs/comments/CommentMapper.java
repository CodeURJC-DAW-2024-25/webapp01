package es.daw01.savex.DTOs.comments;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.daw01.savex.DTOs.CommentDTO;
import es.daw01.savex.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "author", target = "author")
    CommentDTO toDTO(Comment comment);
    List<CommentDTO> toDTO(Collection<Comment> comments);

    Comment toDomain(CommentDTO commentDTO);
    List<Comment> toDomain(Collection<CommentDTO> postDTOs);

    // Simple CommentDTO to Comment conversion
    Comment toDomainSimple(SimpleCommentDTO simpleCommentDTO);
    SimpleCommentDTO toDTOSimple(Comment comment);
}