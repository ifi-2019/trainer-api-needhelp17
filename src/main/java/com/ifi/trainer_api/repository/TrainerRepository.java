package com.ifi.trainer_api.repository;

import com.ifi.trainer_api.bo.Trainer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends CrudRepository<Trainer, String> {

    Trainer save(Trainer trainer);

    Optional<Trainer> findById(String name);

    List<Trainer> findAll();

    long count();

    void delete(Trainer trainer);

    boolean existsById(String name);
}
