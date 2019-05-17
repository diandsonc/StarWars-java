package com.starwars.api.services.impl;

import java.util.List;

import com.starwars.api.models.Planet;
import com.starwars.api.repository.PlanetRepository;
import com.starwars.api.services.PlanetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanetServiceImpl implements PlanetService {

    @Autowired
    private PlanetRepository planetRepository;
    
    @Override
    public List<Planet> getAll() {
        return this.planetRepository.findAll();
    }

    @Override
    public List<Planet> findByName(String name) {
        return this.planetRepository.findByNome(name);
    }

    @Override
    public Planet findById(String id) {
        return this.planetRepository.findById(id).orElse(null);
    }

    @Override
    public Planet add(Planet planet) {
        return this.planetRepository.save(planet);
    }

    @Override
    public Planet update(Planet planet) {
        return this.planetRepository.save(planet);
    }

    @Override
    public void remove(String id) {
        this.planetRepository.deleteById(id);
    }

}