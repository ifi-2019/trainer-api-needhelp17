package com.ifi.trainer_api.bo;

import org.junit.jupiter.api.Test;

import javax.persistence.Embeddable;

import static org.junit.Assert.assertNotNull;

class PokemonTest {

    @Test
    void pokemon_shouldBeAnEmbeddable(){
        assertNotNull(Pokemon.class.getAnnotation(Embeddable.class));
    }

}

