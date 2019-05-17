package com.starwars.api.models.integrations.swapi.models;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SwapiPlanet implements Serializable {

    private static final long serialVersionUID = 1L;

    public String name;
    public String diameter;
    public String climate;
    public String gravity;
    public String terrain;
    public String population;
    public String created;
    public String edited;
    public String url;

    @JsonProperty("rotation_period")
    public String rotationPeriod;

    @JsonProperty("orbital_period")
    public String orbitalPeriod;

    @JsonProperty("surface_water")
    public String surfaceWater;

    @JsonProperty("residents")
    public List<String> residentsUrls;

    @JsonProperty("films")
    public List<String> filmsUrls;
}