package com.chambaya.backend.jobs.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private double latitude;
    private double longitude;
    private String address;
    private String district;

}
