package com.chambaya.backend.iam.application.services;

import com.chambaya.backend.iam.application.commands.CreateUserCommand;
import com.chambaya.backend.iam.application.commands.UpdateProfileCommand;
import com.chambaya.backend.iam.domain.model.Profile;
import com.chambaya.backend.iam.domain.model.User;
import com.chambaya.backend.iam.domain.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserApplicationService {

    private final UserRepository userRepository;

    public UserApplicationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserCommand command){
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Profile profile = new Profile(
                null,
                command.skills(),
                command.experience(),
                command.district(),
                command.phone(),
                false

        );
        User user = new User(
                null,
                command.name(),
                command.email(),
                command.password(),
                command.role(),
                profile,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return userRepository.save(user);

    }

    public User updateProfile(UpdateProfileCommand command){
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Profile profile = new Profile(
                command.photoUrl(),
                command.skills(),
                command.experience(),
                command.district(),
                command.phone(),
                command.verified()
        );
        user.updateProfile(profile);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
