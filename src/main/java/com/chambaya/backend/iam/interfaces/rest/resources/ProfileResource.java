package com.chambaya.backend.iam.interfaces.rest.resources;

import java.util.List;

public record ProfileResource(
        String photoUrl,
        List<String> skills,
        String experience,
        String district,
        String phone,
        boolean verified
) {
}
