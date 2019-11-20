package com.ifi.trainer_api.service;

import com.ifi.trainer_api.bo.Trainer;
import com.ifi.trainer_api.repository.TrainerRepository;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class TrainerServiceImplTest {

    @Test
    void getAllTrainers_shouldCallTheRepository() {
        var trainerRepo = mock(TrainerRepository.class);
        var trainerService = new TrainerServiceImpl(trainerRepo);

        trainerService.getAllTrainers();

        verify(trainerRepo).findAll();
    }

    @Test
    void getTrainer_shouldCallTheRepository() {
        var trainerRepo = mock(TrainerRepository.class);
        var trainerService = new TrainerServiceImpl(trainerRepo);

        trainerService.getTrainer("Ash");

        verify(trainerRepo).findById("Ash");
    }

    @Test
    void createTrainer_shouldCallTheRepository() {
        var trainerRepo = mock(TrainerRepository.class);
        var trainerService = new TrainerServiceImpl(trainerRepo);

        var ash = new Trainer();
        trainerService.createTrainer(ash);

        verify(trainerRepo).save(ash);
    }

    @Test
    void updateTrainer_shouldCreateTrainerIfDoesntExist() {
        var trainerRepo = mock(TrainerRepository.class);
        var trainerService = new TrainerServiceImpl(trainerRepo);

        var ash = new Trainer();
        trainerService.updateTrainer(ash);
        verify(trainerRepo).save(ash);
    }

    @Test
    void updateTrainer_shouldUpdateAshToSasha() {
        var trainerRepo = mock(TrainerRepository.class);
        var trainerService = new TrainerServiceImpl(trainerRepo);

        var ash = new Trainer();
        trainerService.updateTrainer(ash);
        ash.setName("sasha");
        trainerService.updateTrainer(ash);

        verify(trainerRepo, times(2)).save(ash);
    }
}
