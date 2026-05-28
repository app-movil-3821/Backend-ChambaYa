package com.chambaya.backend.iam.application.commands;

public record LoginCommand(
        String email,
        String password
) {
}
