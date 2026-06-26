package com.chambaya.backend.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record GoogleAuthResource(
        @NotBlank
        String idToken
) {
}