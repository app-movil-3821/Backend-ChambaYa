package com.chambaya.backend.favorites.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "favorites")
public class Favorite {
    @Id
    private String id;
    private String workerId;
    private String jobId;
    private LocalDateTime createdAt;
}
