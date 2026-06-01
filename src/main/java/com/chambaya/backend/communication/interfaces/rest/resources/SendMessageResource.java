package com.chambaya.backend.communication.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record SendMessageResource(
        @NotBlank
        String senderId,
        @NotBlank
        String content
) {
}
