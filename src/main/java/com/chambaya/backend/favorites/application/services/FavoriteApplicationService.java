package com.chambaya.backend.favorites.application.services;

import com.chambaya.backend.favorites.application.commands.CreateFavoriteCommand;
import com.chambaya.backend.favorites.domain.model.Favorite;
import com.chambaya.backend.favorites.domain.repositories.FavoriteRepository;
import com.chambaya.backend.iam.application.services.UserApplicationService;
import com.chambaya.backend.iam.domain.model.User;
import com.chambaya.backend.iam.domain.model.UserRole;
import com.chambaya.backend.jobs.application.services.JobApplicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class FavoriteApplicationService {
    private final FavoriteRepository favoriteRepository;
    private final UserApplicationService userApplicationService;
    private final JobApplicationService jobApplicationService;

    public FavoriteApplicationService(
            FavoriteRepository favoriteRepository,
            UserApplicationService userApplicationService,
            JobApplicationService jobApplicationService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.userApplicationService = userApplicationService;
        this.jobApplicationService = jobApplicationService;
    }

    public Favorite createFavorite(CreateFavoriteCommand command) {
        validateWorker(command.workerId());
        validateJobExists(command.jobId());

        boolean alreadySaved = favoriteRepository.findByWorkerId(command.workerId())
                .stream()
                .anyMatch(favorite -> favorite.getJobId().equals(command.jobId()));

        if (alreadySaved) {
            throw new IllegalArgumentException("Job is already saved as favorite by this worker");
        }

        Favorite favorite = new Favorite(
                null,
                command.workerId(),
                command.jobId(),
                LocalDateTime.now()
        );

        return favoriteRepository.save(favorite);
    }

    public Optional<Favorite> findById(String id) {
        return favoriteRepository.findById(id);
    }

    public List<Favorite> findByWorkerId(String workerId) {
        return favoriteRepository.findByWorkerId(workerId);
    }

    public List<Favorite> findAll() {
        return favoriteRepository.findAll();
    }

    public void deleteFavorite(String id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Favorite not found"));

        favoriteRepository.deleteById(favorite.getId());
    }

    private void validateWorker(String workerId) {
        User worker = userApplicationService.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));

        if (worker.getRole() != UserRole.CHAMBEADOR) {
            throw new IllegalArgumentException("User is not a worker");
        }
    }

    private void validateJobExists(String jobId) {
        jobApplicationService.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
    }
}
