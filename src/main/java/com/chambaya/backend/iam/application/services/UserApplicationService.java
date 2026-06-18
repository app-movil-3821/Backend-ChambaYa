package com.chambaya.backend.iam.application.services;

import com.chambaya.backend.iam.application.commands.CreateUserCommand;
import com.chambaya.backend.iam.application.commands.UpdateProfileCommand;
import com.chambaya.backend.iam.domain.model.Profile;
import com.chambaya.backend.iam.domain.model.User;
import com.chambaya.backend.iam.domain.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserApplicationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserApplicationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(CreateUserCommand command){
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        boolean verified = isInstitutionalEmail(command.email());

        Profile profile = new Profile(
                null,
                command.skills(),
                command.experience(),
                command.district(),
                command.phone(),
                verified

        );

        String passwordHash = passwordEncoder.encode(command.password());

        User user = new User(
                null,
                command.name(),
                command.email(),
                passwordHash,
                command.role(),
                profile,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return userRepository.save(user);

    }

    public void changePassword(String userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 8 caracteres");
        }

        user.changePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User updateProfile(UpdateProfileCommand command){        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        boolean verified = isInstitutionalEmail(user.getEmail());
        Profile profile = new Profile(
                command.photoUrl(),
                command.skills(),
                command.experience(),
                command.district(),
                command.phone(),
                verified
        );
        user.updateProfile(profile);
        return userRepository.save(user);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

    private boolean isInstitutionalEmail(String email) {
        if (email == null) {
            return false;
        }

        String normalizedEmail = email.trim().toLowerCase();

        return normalizedEmail.endsWith("@upc.edu.pe")
                || normalizedEmail.endsWith("@alumno.upc.edu.pe");
    }

}