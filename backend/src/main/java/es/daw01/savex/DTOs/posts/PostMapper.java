package es.daw01.savex.DTOs.posts;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.daw01.savex.DTOs.PostDTO;
import es.daw01.savex.model.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDTO toDTO(Post post);
    List<PostDTO> toDTO(Collection<Post> posts);

    @Mapping(target = "banner", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "content", ignore = true)
    Post toDomain(PostDTO postDTO);
    
    List<Post> toDomain(Collection<PostDTO> postDTOs);
}