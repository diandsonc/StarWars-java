package com.starwars.api.models.integrations.swapi;

import java.util.Arrays;

import com.starwars.api.models.integrations.swapi.models.SwapiListPlanet;
import com.starwars.api.models.integrations.swapi.models.SwapiPlanet;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SwapiApi {

    RestTemplate restTemplate;
    HttpHeaders headers;

    public SwapiApi() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", SwapiApiConstants.USER_AGENT_NAME);
    }

    public SwapiPlanet GetPlanet(String name) {
        try {
            HttpEntity<String> entity = new HttpEntity<String>("parameters", this.headers);
            String url = SwapiApiConstants.BASE_URL + "/planets/?search=" + name;
            ResponseEntity<SwapiListPlanet> result = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiListPlanet.class);

            if (result.getStatusCode() == HttpStatus.OK) {
                SwapiListPlanet planets = result.getBody();

                for (SwapiPlanet planet : planets.results) {
                    if (planet.name.equals(name))
                        return planet;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

}