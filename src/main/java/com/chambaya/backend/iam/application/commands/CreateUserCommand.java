package com.chambaya.backend.iam.application.commands;

import com.chambaya.backend.iam.domain.model.UserRole;

import java.util.List;

public record CreateUserCommand(

        String name,
        String email,
        String password,
        UserRole role,
        List<String> skills,
        String experience,
        String district,
        String phone

) {
}
