package com.chambaya.backend.iam.domain.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String email;
    private String passwordHash;
    private UserRole role;
    private Profile profile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public void updateProfile(Profile profile) {
        this.profile = profile;
        this.updatedAt = LocalDateTime.now();
    }
    public void changeRole(UserRole role) {
        this.role = role;
        this.updatedAt = LocalDateTime.now();
    }
    public void changePassword(String passwordHash) {
        this.passwordHash = passwordHash;
        this.updatedAt = LocalDateTime.now();
    }
}