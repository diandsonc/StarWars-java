package com.starwars.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.validation.ConstraintViolationException;

import com.starwars.api.models.Planet;
import com.starwars.api.repository.PlanetRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import({ SpringMongoConfiguration.class })
@SpringBootTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
        this.planetRepository.deleteAll();
    }

	@Test
	public void listAllShould() {
		Planet planetaAlderaan = new Planet("Alderaan", "temperate", "grasslands, mountains");
		Planet planetaHoth = new Planet("Hoth", "frozen", "tundra, ice caves, mountain ranges");

		this.planetRepository.save(planetaAlderaan);
		this.planetRepository.save(planetaHoth);

		List<Planet> planets = this.planetRepository.findAll();

		assertThat(planets).isNotEmpty();
		assertThat(planets.size()).isEqualTo(2);
	}

    @Test
    public void createShouldPersistData() {
        Planet planet = new Planet("Alderaan", "temperate", "grasslands, mountains");

        this.planetRepository.save(planet);

        assertThat(planet.getId()).isNotNull();
        assertThat(planet.getNome()).isEqualTo("Alderaan");
        assertThat(planet.getClima()).isEqualTo("temperate");
        assertThat(planet.getTerreno()).isEqualTo("grasslands, mountains");
    }

    @Test
    public void deleteShouldRemoveData() {
        Planet planet = new Planet("Alderaan", "temperate", "grasslands, mountains");

        this.planetRepository.save(planet);
        this.planetRepository.deleteById(planet.getId());

        assertThat(this.planetRepository.findById(planet.getId()).orElse(null)).isNull();
    }

    @Test
    public void updateShouldChangeAndPersistData() {
        Planet planet = new Planet("Alderaan", "temperate", "grasslands, mountains");

        this.planetRepository.save(planet);
        planet.setNome("Hoth");
        planet.setClima("frozen");
        planet.setTerreno("tundra, ice caves, mountain ranges");
        this.planetRepository.save(planet);
        planet = this.planetRepository.findById(planet.getId()).orElse(null);

        assertThat(planet.getNome()).isEqualTo("Hoth");
        assertThat(planet.getClima()).isEqualTo("frozen");
        assertThat(planet.getTerreno()).isEqualTo("tundra, ice caves, mountain ranges");
    }

	@Test
	public void findByIdShould() {
		Planet planetaAlderan = new Planet("Alderaan", "temperate", "grasslands, mountains");
		Planet planetaHoth = new Planet("Hoth", "frozen", "tundra, ice caves, mountain ranges");

		this.planetRepository.save(planetaAlderan);
		this.planetRepository.save(planetaHoth);

		Planet planet = this.planetRepository.findById(planetaHoth.getId()).orElse(null);

		assertThat(planet).isNotNull();
		assertThat(planet.getNome()).isEqualTo("Hoth");
		assertThat(planet.getClima()).isEqualTo("frozen");
		assertThat(planet.getTerreno()).isEqualTo("tundra, ice caves, mountain ranges");
	}

	@Test
	public void findByNameShould() {
		Planet planetaAlderan = new Planet("Alderaan", "temperate", "grasslands, mountains");
		Planet planetaHoth = new Planet("Hoth", "frozen", "tundra, ice caves, mountain ranges");

		this.planetRepository.save(planetaAlderan);
		this.planetRepository.save(planetaHoth);

		List<Planet> planeta = this.planetRepository.findByNome(planetaAlderan.getNome());

		assertThat(planeta).isNotEmpty();
		assertThat(planeta.size()).isEqualTo(1);
		assertThat(planeta.get(0).getNome()).isEqualTo("Alderaan");
		assertThat(planeta.get(0).getClima()).isEqualTo("temperate");
		assertThat(planeta.get(0).getTerreno()).isEqualTo("grasslands, mountains");
	}

	@Test
	public void createWhenNameIsNullShoudThrowConstraintViolate() {
		thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Campo nome é obrigatório");
        
        Planet planet = new Planet();
        planet.setClima("temperate");
        planet.setTerreno("mountains");

		this.planetRepository.save(planet);
	}

	@Test
	public void createWhenClimaIsNullShoudThrowConstraintViolate() {
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage("Campo clima é obrigatório");
        
        Planet planet = new Planet();
        planet.setNome("Alderaan");
        planet.setTerreno("mountains");

		this.planetRepository.save(planet);
	}

	@Test
	public void createWhenTerrenoIsNullShoudThrowConstraintViolate() {
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage("Campo terreno é obrigatório");
        
        Planet planet = new Planet();
        planet.setNome("Alderaan");
        planet.setClima("temperate");

		this.planetRepository.save(planet);
	}
}