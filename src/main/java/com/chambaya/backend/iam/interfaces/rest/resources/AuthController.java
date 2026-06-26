package com.chambaya.backend.iam.interfaces.rest.resources;

import com.chambaya.backend.iam.application.commands.LoginCommand;
import com.chambaya.backend.iam.application.results.AuthenticatedUserResult;
import com.chambaya.backend.iam.application.services.AuthApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/v1/auth")
public class AuthController {

    private final AuthApplicationService authApplicationService;
    public AuthController(AuthApplicationService authApplicationService){
        this.authApplicationService = authApplicationService;
    }

    @PostMapping("/login")
    public AuthenticatedUserResource login(@Valid @RequestBody LoginResource resource){
        AuthenticatedUserResult result = authApplicationService.login(
                new LoginCommand(
                        resource.email(),
                        resource.password()
                )
        );

        return new AuthenticatedUserResource(
                result.token(),
                result.userId(),
                result.name(),
                result.email(),
                result.role()
        );
    }

    @PostMapping("/google")
    public ResponseEntity<AuthenticatedUserResource> googleAuth(
            @Valid @RequestBody GoogleAuthResource resource
    ) {
        AuthenticatedUserResult result = authApplicationService.googleAuth(resource.idToken());
        return ResponseEntity.ok(new AuthenticatedUserResource(
                result.token(),
                result.userId(),
                result.name(),
                result.email(),
                result.role()
        ));
    }
}