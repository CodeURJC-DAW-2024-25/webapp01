package es.daw01.savex.DTOs.lists;

import java.util.List;

public record listResponse<T> (
    long id,
    String user,
    List<T> data
    )
{}
