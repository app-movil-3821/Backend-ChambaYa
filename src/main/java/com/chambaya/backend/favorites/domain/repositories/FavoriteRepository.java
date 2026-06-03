package com.chambaya.backend.favorites.domain.repositories;

import com.chambaya.backend.favorites.domain.model.Favorite;

import java.util.List;
import java.util.Optional;
public interface FavoriteRepository {
    Favorite save(Favorite favorite);
    Optional<Favorite> findById(String id);
    List<Favorite> findByWorkerId(String workerId);
    boolean existsByWorkerIdAndJobId(String workerId, String jobId);
    void deleteById(String id);
    List<Favorite> findAll();

}
