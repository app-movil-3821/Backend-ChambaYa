package com.chambaya.backend.iam.application.services;

import com.chambaya.backend.iam.application.commands.LoginCommand;
import com.chambaya.backend.iam.application.results.AuthenticatedUserResult;
import com.chambaya.backend.iam.domain.exceptions.InvalidCredentialsException;
import com.chambaya.backend.iam.domain.model.User;
import com.chambaya.backend.iam.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthApplicationService {

    private final UserApplicationService userApplicationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
}
