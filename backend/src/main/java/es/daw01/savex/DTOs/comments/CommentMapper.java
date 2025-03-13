package es.daw01.savex.DTOs.comments;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.daw01.savex.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "author", source = "author.username")
    SimpleCommentDTO toDTOSimple(Comment comment);
    List<SimpleCommentDTO> toDTOSimple(Collection<Comment> comments);
}