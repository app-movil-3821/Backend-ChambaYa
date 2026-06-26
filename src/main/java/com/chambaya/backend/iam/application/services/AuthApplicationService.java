package com.chambaya.backend.iam.application.services;

import com.chambaya.backend.iam.application.commands.CreateUserCommand;
import com.chambaya.backend.iam.application.commands.LoginCommand;
import com.chambaya.backend.iam.application.results.AuthenticatedUserResult;
import com.chambaya.backend.iam.domain.exceptions.InvalidCredentialsException;
import com.chambaya.backend.iam.domain.model.User;
import com.chambaya.backend.iam.domain.model.UserRole;
import com.chambaya.backend.iam.infrastructure.security.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthApplicationService {

    private final UserApplicationService userApplicationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${google.client.id:18412931124-ehfh5ujsq2agndl3kcekmg8u8jsa9emj.apps.googleusercontent.com}")
    private String googleClientId;

    public AuthApplicationService(
            UserApplicationService userApplicationService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ){
        this.userApplicationService = userApplicationService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthenticatedUserResult login(LoginCommand command){
        User user = userApplicationService.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(command.password(), user.getPasswordHash())){
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);

        return new AuthenticatedUserResult(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public AuthenticatedUserResult googleAuth(String idToken) {
        try {
            // 1. Verificar el token con Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                throw new IllegalArgumentException("Token de Google inválido.");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String name  = (String) payload.get("name");
            if (name == null || name.isBlank()) name = email.split("@")[0];

            // 2. Buscar usuario existente o crearlo
            Optional<User> existing = userApplicationService.findByEmail(email);
            User user;

            if (existing.isPresent()) {
                user = existing.get();
            } else {
                // Crear usuario nuevo con contraseña aleatoria (no necesita login con password)
                user = userApplicationService.createUser(new CreateUserCommand(
                        name,
                        email,
                        UUID.randomUUID().toString(), // password aleatorio
                        UserRole.CHAMBEADOR,           // rol por defecto
                        List.of(),
                        "",
                        "",
                        ""
                ));
            }

            String token = jwtService.generateToken(user);
            return new AuthenticatedUserResult(
                    token,
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().name()
            );

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al autenticar con Google: " + e.getMessage());
        }
    }
}