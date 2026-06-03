package com.chambaya.backend.favorites.interfaces.rest.controllers;


import com.chambaya.backend.favorites.application.services.FavoriteApplicationService;
import com.chambaya.backend.favorites.domain.model.Favorite;
import com.chambaya.backend.favorites.interfaces.rest.assemblers.FavoriteResourceAssembler;
import com.chambaya.backend.favorites.interfaces.rest.resources.CreateFavoriteResource;
import com.chambaya.backend.favorites.interfaces.rest.resources.FavoriteResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {
    private final FavoriteApplicationService favoriteApplicationService;

    public FavoriteController(FavoriteApplicationService favoriteApplicationService) {
        this.favoriteApplicationService = favoriteApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteResource createFavorite(@Valid @RequestBody CreateFavoriteResource resource) {
        Favorite favorite = favoriteApplicationService.createFavorite(
                FavoriteResourceAssembler.toCreateFavoriteCommand(resource)
        );

        return FavoriteResourceAssembler.toResource(favorite);
    }

    @GetMapping
    public List<FavoriteResource> getAllFavorites() {
        return favoriteApplicationService.findAll()
                .stream()
                .map(FavoriteResourceAssembler::toResource)
                .toList();
    }

    @GetMapping("/{id}")
    public FavoriteResource getFavoriteById(@PathVariable String id) {
        Favorite favorite = favoriteApplicationService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite not found"));

        return FavoriteResourceAssembler.toResource(favorite);
    }

    @GetMapping("/worker/{workerId}")
    public List<FavoriteResource> getFavoritesByWorkerId(@PathVariable String workerId) {
        return favoriteApplicationService.findByWorkerId(workerId)
                .stream()
                .map(FavoriteResourceAssembler::toResource)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavorite(@PathVariable String id) {
        favoriteApplicationService.deleteFavorite(id);
    }
}
