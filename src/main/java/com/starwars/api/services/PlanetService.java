package com.starwars.api.services;

import java.util.List;

import com.starwars.api.models.Planet;

public interface PlanetService {
    List<Planet> getAll();

    List<Planet> findByName(String name);

    Planet findById(String id);

    Planet add(Planet planet);

    Planet update(Planet planet);

    void remove(String id);
}