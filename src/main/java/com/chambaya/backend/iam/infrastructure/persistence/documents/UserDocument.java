package com.chambaya.backend.iam.infrastructure.persistence.documents;

import com.chambaya.backend.iam.domain.model.UserRole;
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
@Document(collection = "users")
public class UserDocument {
    @Id
    private String id;
    private String name;
    private String email;
    private String passwordHash;
    private UserRole role;
    private ProfileDocument profile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
