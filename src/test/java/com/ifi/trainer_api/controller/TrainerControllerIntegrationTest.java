package com.ifi.trainer_api.controller;

import com.ifi.trainer_api.bo.Pokemon;
import com.ifi.trainer_api.bo.Trainer;
import com.ifi.trainer_api.service.TrainerService;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrainerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TrainerController controller;

    @Autowired
    private TrainerService trainerService;

    @Test
    void trainerController_shouldBeInstanciated(){
        assertNotNull(controller);
        assertNotNull(trainerService);
    }

    @Test
    void getTrainer_withNameAsh_shouldReturnAsh() {
        var ash = this.restTemplate.getForObject("http://localhost:" + port + "/trainers/Ash", Trainer.class);
        assertNotNull(ash);
        assertEquals("Ash", ash.getName());
        assertEquals(1, ash.getTeam().size());

        assertEquals(25, ash.getTeam().get(0).getPokemonType());
        assertEquals(18, ash.getTeam().get(0).getLevel());
    }

    @Test
    void getAllTrainers_shouldReturnAshAndMisty() {
        var trainers = this.restTemplate.getForObject("http://localhost:" + port + "/trainers/", Trainer[].class);
        assertNotNull(trainers);
        assertEquals(2, trainers.length);

        assertEquals("Ash", trainers[0].getName());
        assertEquals("Misty", trainers[1].getName());
    }

    @Test
    void updateTrainers_shouldReturn3pokemons() throws ParseException {
        var trainerNull = trainerService.getTrainer("Bug Catcher");
        assertNull(trainerNull);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{\"name\": \"Bug Catcher\",\"team\": [ {\"pokemonType\": 13, \"level\": 6}, {\"pokemonType\": 10, \"level\": 6}] }", headers);
        var trainerFind = this.restTemplate.postForObject("http://localhost:" + port + "/trainers/", entity,Trainer.class);

        assertNotNull(trainerFind);
        assertEquals(2, trainerFind.getTeam().size());

        HttpEntity<String> entity2 = new HttpEntity<>("{\"name\": \"Bug Catcher\",\"team\": [ {\"pokemonType\": 13, \"level\": 6}, {\"pokemonType\": 10, \"level\": 6}, {\"pokemonType\": 17, \"level\": 61}] }", headers);
        var trainerFindAndUpdate = this.restTemplate.postForObject("http://localhost:" + port + "/trainers/", entity2,Trainer.class);
        assertEquals(trainerFindAndUpdate.getTeam().size(),3);
        var trainer2 = trainerService.getTrainer("Bug Catcher");
        assertEquals(3,trainer2.getTeam().size());
        assertEquals(3, trainerFind.getTeam().size());
    }

}
