package com.chambaya.backend.iam.interfaces.rest.resources;

import com.chambaya.backend.iam.domain.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateUserResource(

        @NotBlank
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password,

        @NotNull
        UserRole role,

        List<String> skills,
        String experience,
        String district,
        String phone

) {
}
