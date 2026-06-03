package com.chambaya.backend.favorites.interfaces.rest.assemblers;

import com.chambaya.backend.favorites.application.commands.CreateFavoriteCommand;
import com.chambaya.backend.favorites.domain.model.Favorite;
import com.chambaya.backend.favorites.interfaces.rest.resources.CreateFavoriteResource;
import com.chambaya.backend.favorites.interfaces.rest.resources.FavoriteResource;

public class FavoriteResourceAssembler {
    private FavoriteResourceAssembler() {
    }

    public static FavoriteResource toResource(Favorite favorite) {
        return new FavoriteResource(
                favorite.getId(),
                favorite.getWorkerId(),
                favorite.getJobId(),
                favorite.getCreatedAt()
        );
    }

    public static CreateFavoriteCommand toCreateFavoriteCommand(CreateFavoriteResource resource) {
        return new CreateFavoriteCommand(
                resource.workerId(),
                resource.jobId()
        );
    }
}
