package com.chambaya.backend.iam.application.commands;

import java.util.List;

public record UpdateProfileCommand(

        String userId,
        String photoUrl,
        List<String> skills,
        String experience,
        String district,
        String phone,
        boolean verified
) {
}
