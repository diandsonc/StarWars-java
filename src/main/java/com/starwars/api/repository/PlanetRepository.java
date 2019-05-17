package com.starwars.api.repository;

import java.util.List;

import com.starwars.api.models.Planet;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlanetRepository extends MongoRepository<Planet, String> {
    List<Planet> findByNome(String nome);
}