package com.ifi.trainer_api.bo;

import org.junit.jupiter.api.Test;

import javax.persistence.*;

import static org.junit.jupiter.api.Assertions.*;

class TrainerTest {

    @Test
    void trainer_shouldBeAnEntity(){
        assertNotNull(Trainer.class.getAnnotation(Entity.class));
    }

    @Test
    void trainerName_shouldBeAnId() throws NoSuchFieldException {
        assertNotNull(Trainer.class.getDeclaredField("name").getAnnotation(Id.class));
    }

    @Test
    void trainerTeam_shouldBeAElementCollection() throws NoSuchFieldException {
        assertNotNull(Trainer.class.getDeclaredField("team").getAnnotation(ElementCollection.class));
    }

}
