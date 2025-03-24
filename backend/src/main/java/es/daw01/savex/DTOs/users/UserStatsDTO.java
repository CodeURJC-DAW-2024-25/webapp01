package es.daw01.savex.DTOs.users;

import java.util.List;

public record UserStatsDTO(
    List<Integer> usersPerMonth
) {}