package com.starwars.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.starwars.api.models.integrations.swapi.SwapiApi;
import com.starwars.api.models.integrations.swapi.models.SwapiPlanet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SwapiApiTests {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getAllPlanets() throws Exception {

        SwapiApi swapi = new SwapiApi(); 
        SwapiPlanet planet = swapi.GetPlanet("Alderaan");

        assertThat(planet).isNotNull();
        assertThat(planet.name).isEqualTo("Alderaan");
        assertThat(planet.climate).isEqualTo("temperate");
        log.debug(String.format("Name: %-25s Climate: %-25s Pop: %16s", planet.name, planet.climate, planet.population));
    }
}