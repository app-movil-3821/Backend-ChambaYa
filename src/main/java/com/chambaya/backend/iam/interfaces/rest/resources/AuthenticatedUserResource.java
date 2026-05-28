package com.chambaya.backend.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(
        String token,
        String userId,
        String name,
        String email,
        String role
) {
}
