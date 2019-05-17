package com.starwars.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.starwars.api.models.Planet;
import com.starwars.api.models.integrations.swapi.SwapiApi;
import com.starwars.api.models.integrations.swapi.models.SwapiPlanet;
import com.starwars.api.repository.PlanetRepository;
import com.starwars.api.responses.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import mockit.Mock;
import mockit.MockUp;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PlanetEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PlanetRepository planetRepository;

    @Before
    public void Setup() {
        Optional<Planet> planet = Optional.of(new Planet("6cde21787c63594ca86852f2", "Hoth", "frozen", "tundra, ice caves, mountain ranges"));
        BDDMockito.when(planetRepository.findById(planet.get().getId())).thenReturn(planet);
    }

    @Before
    public void MockSwapi() {
        new MockUp<SwapiApi>() {
            @Mock
            public SwapiPlanet GetPlanet(String name) {
                SwapiPlanet planet = new SwapiPlanet();
                planet.name = name;
                planet.climate = "temperate";
                planet.filmsUrls = Arrays.asList("1", "2", "5");
                
                return planet;
            }
        };
    }
    @Test
    public void listPlanetsShouldReturnStatusCode200() {
        List<Planet> planets = new ArrayList<Planet>();
        planets.add(new Planet("5cde21787c63594ca86852f1", "Alderaan", "temperate", "grasslands, mountains"));
        planets.add(new Planet("6cde21787c63594ca86852f2", "Hoth", "frozen", "tundra, ice caves, mountain ranges"));
        BDDMockito.when(planetRepository.findAll()).thenReturn(planets);

        ParameterizedTypeReference<Response<List<Planet>>> responseType = new ParameterizedTypeReference<Response<List<Planet>>>() {};
        ResponseEntity<Response<List<Planet>>> response = restTemplate.exchange("/v1/planets", HttpMethod.GET, null, responseType);
                    
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getData()).isNotNull();
        assertThat(response.getBody().getData().size()).isEqualTo(2);
    }
    
    @Test
    public void getPlanetByNameShouldReturnStatusCode200() {
        List<Planet> planets = new ArrayList<Planet>();
        planets.add(new Planet("5cde21787c63594ca86852f1", "Alderaan", "temperate", "grasslands, mountains"));
        BDDMockito.when(planetRepository.findByNome(planets.get(0).getNome())).thenReturn(planets);

        ParameterizedTypeReference<Response<List<Planet>>> responseType = new ParameterizedTypeReference<Response<List<Planet>>>() {};
        ResponseEntity<Response<List<Planet>>> response = restTemplate.exchange("/v1/planets/search/name/{name}", HttpMethod.GET, null, responseType, "Alderaan");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getData()).isNotNull();
        assertThat(response.getBody().getData().size()).isEqualTo(1);
    }

    @Test
    public void getPlanetByIdShouldReturnStatusCode200() {
        ResponseEntity<Planet> response = restTemplate.getForEntity("/v1/planets/search/id/{id}", Planet.class, "6cde21787c63594ca86852f2");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getNome()).isNotNull();
    }

    @Test
    public void getPlanetByIdWhenPlanetDoesNotExistShouldReturnStatusCode404() {
        ResponseEntity<Planet> response = restTemplate.getForEntity("/v1/planets/search/id/{id}", Planet.class, "5cde21787c63594ca86852f1");
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void deleteByIdShouldReturnStatusCode204() {
        BDDMockito.doNothing().when(planetRepository).deleteById("6cde21787c63594ca86852f2");        
        ResponseEntity<String> exchange = restTemplate.exchange("/v1/planets/{id}", HttpMethod.DELETE, null, String.class, "6cde21787c63594ca86852f2");
        assertThat(exchange.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    public void deleteByIdShouldReturnStatusCode404() {
        BDDMockito.doNothing().when(planetRepository).deleteById("6cde21787c63594ca86852f2");        
        ResponseEntity<String> exchange = restTemplate.exchange("/v1/planets/{id}", HttpMethod.DELETE, null, String.class, "5cde21787c63594ca86852f1");
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createWhenNameIsNullShoudReturnStatusCode400BadRequest() throws Exception {
        Planet planet = new Planet(null, "frozen", "tundra, ice caves, mountain ranges");
        BDDMockito.when(planetRepository.save(planet)).thenReturn(planet);
        
        ResponseEntity<String> response = restTemplate.postForEntity("/v1/planets", planet, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).contains("error", "Campo nome é obrigatório");
    }

    @Test
    public void createWhenClimaIsNullShoudReturnStatusCode400BadRequest() throws Exception {
        Planet planet = new Planet("Hoth", null, "tundra, ice caves, mountain ranges");
        BDDMockito.when(planetRepository.save(planet)).thenReturn(planet);
        
        ResponseEntity<String> response = restTemplate.postForEntity("/v1/planets", planet, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).contains("error", "Campo clima é obrigatório");
    }

    @Test
    public void createWhenTerrenoIsNullShoudReturnStatusCode400BadRequest() throws Exception {
        Planet planet = new Planet("Hoth", "frozen", null);
        BDDMockito.when(planetRepository.save(planet)).thenReturn(planet);        
        
        ResponseEntity<String> response = restTemplate.postForEntity("/v1/planets", planet, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).contains("error", "Campo terreno é obrigatório");
    }

    @Test
    public void createShouldPersistDataAndReturnStatusCode200() throws Exception {
        Planet planet = new Planet("5cde21787c63594ca86852f1", "Alderaan", "temperate", "grasslands, mountains");
        BDDMockito.when(planetRepository.save(planet)).thenReturn(planet);

        HttpEntity<Planet> requestEntity = new HttpEntity<Planet>(planet);
        ParameterizedTypeReference<Response<Planet>> responseType = new ParameterizedTypeReference<Response<Planet>>() {};
        ResponseEntity<Response<Planet>> response = restTemplate.exchange("/v1/planets", HttpMethod.POST, requestEntity, responseType);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getData().getId()).isNotNull();
    }

    @Test
    public void updateWhenNameIsNullShoudReturnStatusCode400BadRequest() throws Exception {
        Planet planet = new Planet(null, "frozen", "tundra, ice caves, mountain ranges");
        BDDMockito.when(planetRepository.save(planet)).thenReturn(planet);
        
        ParameterizedTypeReference<Response<Planet>> responseType = new ParameterizedTypeReference<Response<Planet>>() {};
        HttpEntity<Planet> requestEntity = new HttpEntity<Planet>(planet);
        ResponseEntity<Response<Planet>> exchange = restTemplate.exchange("/v1/planets/{id}", HttpMethod.PUT, requestEntity, responseType, "6cde21787c63594ca86852f2");
        assertThat(exchange.getStatusCodeValue()).isEqualTo(400);
        assertThat(exchange.getBody().getErrors()).isNotNull();
        assertThat(exchange.getBody().getErrors()).contains("Campo nome é obrigatório");
    }

    @Test
    public void updateWhenClimaIsNullShoudReturnStatusCode400BadRequest() throws Exception {
        Planet planet = new Planet("Hoth", null, "tundra, ice caves, mountain ranges");
        BDDMockito.when(planetRepository.save(planet)).thenReturn(planet);
        
        ParameterizedTypeReference<Response<Planet>> responseType = new ParameterizedTypeReference<Response<Planet>>() {};
        HttpEntity<Planet> requestEntity = new HttpEntity<Planet>(planet);
        ResponseEntity<Response<Planet>> exchange = restTemplate.exchange("/v1/planets/{id}", HttpMethod.PUT, requestEntity, responseType, "6cde21787c63594ca86852f2");
        assertThat(exchange.getStatusCodeValue()).isEqualTo(400);
        assertThat(exchange.getBody().getErrors()).isNotNull();
        assertThat(exchange.getBody().getErrors()).contains("Campo clima é obrigatório");
    }

    @Test
    public void updateWhenTerrenoIsNullShoudReturnStatusCode400BadRequest() throws Exception {
        Planet planet = new Planet("Hoth", "frozen", null);
        BDDMockito.when(planetRepository.save(planet)).thenReturn(planet);
        
        ParameterizedTypeReference<Response<Planet>> responseType = new ParameterizedTypeReference<Response<Planet>>() {};
        HttpEntity<Planet> requestEntity = new HttpEntity<Planet>(planet);
        ResponseEntity<Response<Planet>> exchange = restTemplate.exchange("/v1/planets/{id}", HttpMethod.PUT, requestEntity, responseType, "6cde21787c63594ca86852f2");
        assertThat(exchange.getStatusCodeValue()).isEqualTo(400);
        assertThat(exchange.getBody().getErrors()).isNotNull();
        assertThat(exchange.getBody().getErrors()).contains("Campo terreno é obrigatório");
    }

    @Test
    public void updateWhenIdNotFoundShoudReturnStatusCode400BadRequest() throws Exception {
        Planet planet = new Planet("Hoth", "frozen", "tundra, ice caves, mountain ranges");
        BDDMockito.when(planetRepository.save(planet)).thenReturn(planet);
        
        HttpEntity<Planet> requestEntity = new HttpEntity<Planet>(planet);
        ResponseEntity<String> exchange = restTemplate.exchange("/v1/planets/{id}", HttpMethod.PUT, requestEntity, String.class, "5cde21787c63594ca86852f1");
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void updateShouldPersistDataAndReturnStatusCode200() throws Exception {
        Planet planet = new Planet("6cde21787c63594ca86852f2", "Alderaan", "temperate", "grasslands, mountains");
        BDDMockito.when(planetRepository.save(planet)).thenReturn(planet);
        
        HttpEntity<Planet> requestEntity = new HttpEntity<Planet>(planet);
        ParameterizedTypeReference<Response<Planet>> responseType = new ParameterizedTypeReference<Response<Planet>>() {};
        ResponseEntity<Response<Planet>> response = restTemplate.exchange("/v1/planets/{id}", HttpMethod.PUT, requestEntity, responseType, "6cde21787c63594ca86852f2");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getData().getId()).isNotNull();
    }
}