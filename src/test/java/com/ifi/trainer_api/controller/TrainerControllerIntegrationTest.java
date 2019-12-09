package com.ifi.trainer_api.controller;

import com.ifi.trainer_api.bo.Pokemon;
import com.ifi.trainer_api.bo.Trainer;
import com.ifi.trainer_api.service.TrainerService;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
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

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Test
    void getTrainers_shouldThrowAnUnauthorized(){
        var responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/trainers/Ash", Trainer.class);
        assertNotNull(responseEntity);
        assertEquals(401, responseEntity.getStatusCodeValue());
    }

    @Test
    void getTrainer_withNameAsh_shouldReturnAsh() {
        System.out.println("usre = "+ this.username+ "  pass  "+this.password);
        var ash = this.restTemplate.withBasicAuth(username, password).getForObject("http://localhost:" + port + "/trainers/Ash", Trainer.class);

        assertNotNull(ash);
        assertEquals("Ash", ash.getName());
        assertEquals(1, ash.getTeam().size());

        assertEquals(25, ash.getTeam().get(0).getPokemonType());
        assertEquals(18, ash.getTeam().get(0).getLevel());
    }


    @Test
    void trainerController_shouldBeInstanciated(){
        assertNotNull(controller);
        assertNotNull(trainerService);
    }

    @Test
    void getAllTrainers_shouldReturnAshAndMisty() {
        var trainers = this.restTemplate
                .withBasicAuth(username, password)
                .getForObject("http://localhost:" + port + "/trainers/", Trainer[].class);

        assertNotNull(trainers);
        assertEquals(3, trainers.length);

        assertEquals("Ash", trainers[0].getName());
        assertEquals("Misty", trainers[2].getName());
    }

    @Test
    void updateTrainers_shouldReturn3pokemons() throws ParseException {
        var trainerNull = trainerService.getTrainer("Bug Catcher");
        assertNull(trainerNull);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{\"name\": \"Bug Catcher\",\"team\": [ {\"pokemonType\": 13, \"level\": 6}, {\"pokemonType\": 10, \"level\": 6}] }", headers);
        var trainerFind = this.restTemplate
                .withBasicAuth(username, password)
                .postForObject("http://localhost:" + port + "/trainers/", entity,Trainer.class);

        assertNotNull(trainerFind);
        assertEquals(2, trainerFind.getTeam().size());

        HttpEntity<String> entity2 = new HttpEntity<>("{\"name\": \"Bug Catcher\",\"team\": [ {\"pokemonType\": 13, \"level\": 6}, {\"pokemonType\": 10, \"level\": 6}, {\"pokemonType\": 17, \"level\": 61}] }", headers);
        var trainerFindAndUpdate = this.restTemplate
                .withBasicAuth(username, password)
                .postForObject("http://localhost:" + port + "/trainers/", entity2,Trainer.class);
        assertEquals(trainerFindAndUpdate.getTeam().size(),3);

        // validation en jouant un GET
        var trainers = this.restTemplate
                .withBasicAuth(username, password)
                .getForObject("http://localhost:" + port + "/trainers/", Trainer[].class);
        assertEquals(3, trainers.length);

        assertEquals("Ash", trainers[0].getName());
        assertEquals("Bug Catcher", trainers[1].getName());
        assertEquals("Misty", trainers[2].getName());
        assertEquals(3, trainers[1].getTeam().size());
    }

}
