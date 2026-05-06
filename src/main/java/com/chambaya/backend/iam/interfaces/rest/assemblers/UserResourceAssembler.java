package com.chambaya.backend.iam.interfaces.rest.assemblers;

import com.chambaya.backend.iam.application.commands.CreateUserCommand;
import com.chambaya.backend.iam.application.commands.UpdateProfileCommand;
import com.chambaya.backend.iam.domain.model.Profile;
import com.chambaya.backend.iam.domain.model.User;
import com.chambaya.backend.iam.interfaces.rest.resources.CreateUserResource;
import com.chambaya.backend.iam.interfaces.rest.resources.ProfileResource;
import com.chambaya.backend.iam.interfaces.rest.resources.UpdateProfileResource;
import com.chambaya.backend.iam.interfaces.rest.resources.UserResource;

public class UserResourceAssembler {

    private UserResourceAssembler() {}

    public static CreateUserCommand toCreateUserCommand(CreateUserResource resource){
        return new CreateUserCommand(
                resource.name(),
                resource.email(),
                resource.password(),
                resource.role(),
                resource.skills(),
                resource.experience(),
                resource.district(),
                resource.phone()
        );
    }

    public static UpdateProfileCommand toUpdateProfileCommand(String userId, UpdateProfileResource resource){
        return new UpdateProfileCommand(
                userId,
                resource.photoUrl(),
                resource.skills(),
                resource.experience(),
                resource.district(),
                resource.phone(),
                resource.verified()
        );
    }

    public static UserResource toResource(User user){
        return new UserResource(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                toProfileResource(user.getProfile()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private static ProfileResource toProfileResource(Profile profile) {
        if (profile == null) {
            return null;
        }
        return new ProfileResource(
                profile.getPhotoUrl(),
                profile.getSkills(),
                profile.getExperience(),
                profile.getDistrict(),
                profile.getPhone(),
                profile.isVerified()
        );
    }
}
