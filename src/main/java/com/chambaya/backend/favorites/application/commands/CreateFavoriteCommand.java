package com.chambaya.backend.favorites.application.commands;

public record CreateFavoriteCommand(
        String workerId,
        String jobId
) {
}
