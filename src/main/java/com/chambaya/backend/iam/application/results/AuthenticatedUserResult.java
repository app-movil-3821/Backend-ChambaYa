package com.chambaya.backend.iam.application.results;

public record AuthenticatedUserResult(
        String token,
        String userId,
        String name,
        String email,
        String role
) {
}
