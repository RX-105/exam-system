package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Visits;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
public class TestVisitsRepository {
    @Autowired
    VisitsRepository repository;

    @Test
    void testInsert(){
        Optional<Visits> optionalVisits = repository.findById(LocalDate.now());
        Visits visits;
        if (optionalVisits.isPresent()){
            visits = optionalVisits.get();
            visits.setCount(visits.getCount() + 1);
        } else {
            visits = new Visits(LocalDate.now(), 1L);
        }
        repository.save(visits);
    }
}
