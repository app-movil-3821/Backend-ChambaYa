package com.chambaya.backend.jobs.interfaces.rest.resources;

public record LocationResource(
        double latitude,
        double longitude,
        String address,
        String district
) {
}
