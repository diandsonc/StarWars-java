package com.starwars.api.models.integrations.swapi.models;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapiListPlanet implements Serializable {

    private static final long serialVersionUID = 1L;

    public int count;
    public String next;
    public String previous;
    public List<SwapiPlanet> results;

}