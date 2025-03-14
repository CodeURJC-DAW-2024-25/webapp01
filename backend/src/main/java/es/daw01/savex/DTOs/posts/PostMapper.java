package es.daw01.savex.DTOs.posts;

import java.util.Collection;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import es.daw01.savex.DTOs.PostDTO;
import es.daw01.savex.model.Post;
import es.daw01.savex.utils.DateUtils;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDTO toDTO(Post post);
    List<PostDTO> toDTO(Collection<Post> posts);

    @Mapping(target = "banner", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "content", ignore = true)
    Post toDomain(PostDTO postDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "banner", ignore = true)
    @Mapping(target = "comments", expression = "java(new ArrayList<>())")
    @Mapping(target = "readingTime", expression = "java(\"0 min\")")
    Post toDomain(CreatePostRequest createPostRequest);
    
    List<Post> toDomain(Collection<PostDTO> postDTOs);
    
    @Mapping(target = "banner", ignore = true)
    @Mapping(target = "comments", expression = "java(new ArrayList<>())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "readingTime", expression = "java(\"0 min\")")
    void createPostFromRequest(CreatePostRequest createPostRequest, @MappingTarget Post post);

    @AfterMapping
    default void afterMapping(CreatePostRequest createPostRequest, @MappingTarget Post post) {
        post.setDate(DateUtils.formatDate(DateUtils.now()));
        post.calulateReadingTime();
    }
}