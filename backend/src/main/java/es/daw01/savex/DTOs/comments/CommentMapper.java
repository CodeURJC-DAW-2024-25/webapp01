package es.daw01.savex.DTOs.comments;

import java.util.Collection;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import es.daw01.savex.model.Comment;
import es.daw01.savex.model.Post;
import es.daw01.savex.model.User;
import es.daw01.savex.utils.DateUtils;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "author", source = "author.username")
    SimpleCommentDTO toDTOSimple(Comment comment);
    List<SimpleCommentDTO> toDTOSimple(Collection<Comment> comments);

    @Mapping(target = "post", source = "post")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "content", source = "request.content")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "formatedDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toDomain(CreateCommentRequest request, User author, Post post);

    @AfterMapping
    default void toDomain(@MappingTarget Comment comment, CreateCommentRequest request, User author, Post post) {
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setFormatedDate(DateUtils.format(DateUtils.now()));
        comment.setCreatedAt(DateUtils.now());
    }
}