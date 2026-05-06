package com.chambaya.backend.iam.infrastructure.persistence.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDocument {

    private String photoUrl;
    private List<String> Skills;
    private String experience;
    private String district;
    private String phone;
    private boolean verified;

}
