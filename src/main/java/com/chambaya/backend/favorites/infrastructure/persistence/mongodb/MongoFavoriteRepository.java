package com.chambaya.backend.favorites.infrastructure.persistence.mongodb;

import com.chambaya.backend.favorites.domain.model.Favorite;
import com.chambaya.backend.favorites.domain.repositories.FavoriteRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoFavoriteRepository
        extends MongoRepository<Favorite, String>, FavoriteRepository {
}
