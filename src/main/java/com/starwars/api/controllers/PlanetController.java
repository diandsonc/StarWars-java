package com.starwars.api.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.starwars.api.models.Planet;
import com.starwars.api.responses.Response;
import com.starwars.api.services.PlanetService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/planets")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @GetMapping
    public ResponseEntity<Response<List<Planet>>> getAll() {

        List<Planet> planets = this.planetService.getAll();

        return ResponseEntity.ok(new Response<List<Planet>>(planets));
    }

    @GetMapping(path = "/search/id/{id}")
    public ResponseEntity<Planet> findById(@PathVariable(name = "id") String id) {

        Planet planet = this.planetService.findById(id);

        if (planet == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(planet);
    }

    @GetMapping(path = "/search/name/{name}")
    public ResponseEntity<Response<List<Planet>>> findByName(@PathVariable(name = "name") String name) {

        List<Planet> planets = this.planetService.findByName(name);

        return ResponseEntity.ok(new Response<List<Planet>>(planets));
    }

    @PostMapping
    public ResponseEntity<Response<Planet>> add(@Valid @RequestBody Planet data, BindingResult result) {

        if (result.hasErrors()) {
            List<String> errors = new ArrayList<String>();

            result.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(new Response<Planet>(errors));
        }

        Planet planet = this.planetService.add(data);

        return ResponseEntity.ok(new Response<Planet>(planet));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Response<Planet>> update(@PathVariable String id, @Valid @RequestBody Planet data,
            BindingResult result) {

        if (result.hasErrors()) {
            List<String> errors = new ArrayList<String>();

            result.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(new Response<Planet>(errors));
        }

        Planet planetExistent = this.planetService.findById(id);

        if (planetExistent == null) {
            return ResponseEntity.notFound().build();
        }

        BeanUtils.copyProperties(data, planetExistent, "id");

        this.planetService.update(planetExistent);

        return ResponseEntity.ok(new Response<Planet>(planetExistent));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> remove(@PathVariable(name = "id") String id) {

        Planet planet = this.planetService.findById(id);

        if (planet == null) {
            return ResponseEntity.notFound().build();
        }

        this.planetService.remove(id);

        return ResponseEntity.noContent().build();
    }
}