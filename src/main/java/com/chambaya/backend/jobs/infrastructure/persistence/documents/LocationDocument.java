package com.chambaya.backend.jobs.infrastructure.persistence.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDocument {

    private double latitude;
    private double longitude;
    private String address;
    private String district;
}
