package com.chambaya.backend.iam.interfaces.rest.controllers;



import com.chambaya.backend.iam.application.services.UserApplicationService;
import com.chambaya.backend.iam.domain.model.User;
import com.chambaya.backend.iam.interfaces.rest.assemblers.UserResourceAssembler;
import com.chambaya.backend.iam.interfaces.rest.resources.CreateUserResource;
import com.chambaya.backend.iam.interfaces.rest.resources.ChangePasswordResource;
import com.chambaya.backend.iam.interfaces.rest.resources.UpdateProfileResource;
import com.chambaya.backend.iam.interfaces.rest.resources.UserResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResource creatUser(@Valid @RequestBody CreateUserResource resource){
        User user = userApplicationService.createUser(
                UserResourceAssembler.toCreateUserCommand(resource)
        );
        return UserResourceAssembler.toResource(user);
    }

    @GetMapping
    public List<UserResource> getAllUsers(){
        return userApplicationService.findAll()
                .stream()
                .map(UserResourceAssembler::toResource)
                .toList();

    }

    @GetMapping("/{id}")
    public UserResource getUserById(@PathVariable String id){
        User user = userApplicationService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return UserResourceAssembler.toResource(user);
    }

    @GetMapping("/by-email")
    public UserResource getUserByEmail(@RequestParam String email){
        User user = userApplicationService.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return UserResourceAssembler.toResource(user);
    }

    @PutMapping("/{id}/profile")
    public UserResource updateProfile(
            @PathVariable String id,
            @RequestBody UpdateProfileResource resource
    ){
        User user = userApplicationService.updateProfile(
                UserResourceAssembler.toUpdateProfileCommand(id, resource)
        );
        return UserResourceAssembler.toResource(user);
    }

    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @PathVariable String id,
            @Valid @RequestBody ChangePasswordResource resource
    ){
        userApplicationService.changePassword(id, resource.currentPassword(), resource.newPassword());
    }
}