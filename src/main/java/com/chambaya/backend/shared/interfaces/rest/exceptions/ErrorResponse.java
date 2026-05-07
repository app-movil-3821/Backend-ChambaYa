package com.chambaya.backend.shared.interfaces.rest.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(

        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp

) {
}
