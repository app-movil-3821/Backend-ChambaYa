package com.chambaya.backend.iam.infrastructure.persistence.mappers;

import com.chambaya.backend.iam.domain.model.Profile;
import com.chambaya.backend.iam.domain.model.User;
import com.chambaya.backend.iam.infrastructure.persistence.documents.UserDocument;
import com.chambaya.backend.iam.infrastructure.persistence.documents.ProfileDocument;


public class UserMapper {

    private UserMapper() {}
    public static UserDocument toDocument(User user) {
        if (user == null) {
            return null;
        }
        return new UserDocument(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole(),
                toProfileDocument(user.getProfile()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static User toDomain(UserDocument document) {
        if (document == null) {
            return null;
        }
        return new User(
                document.getId(),
                document.getName(),
                document.getEmail(),
                document.getPasswordHash(),
                document.getRole(),
                toProfileDomain(document.getProfile()),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );

    }

    private static ProfileDocument toProfileDocument(Profile profile) {
        if (profile == null) {
            return null;
        }
        return new ProfileDocument(
                profile.getPhotoUrl(),
                profile.getSkills(),
                profile.getExperience(),
                profile.getDistrict(),
                profile.getPhone(),
                profile.isVerified()
        );
    }

    private static Profile toProfileDomain(ProfileDocument document) {
        if (document == null) {
            return null;
        }
        return new Profile(
                document.getPhotoUrl(),
                document.getSkills(),
                document.getExperience(),
                document.getDistrict(),
                document.getPhone(),
                document.isVerified()
        );
    }

}
