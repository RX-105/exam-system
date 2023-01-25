package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Visits;
import io.n0sense.examsystem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
public class TestVisitsRepository {
    @Autowired
    VisitsRepository repository;
    @Autowired
    HibernateUtil hibernateUtil;

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

    @Test
    void testSession(){
        Session session = hibernateUtil.getSessionJavaConfigFactory().openSession();
        Transaction transaction = session.beginTransaction();
        BigDecimal result = session
                .createNativeQuery("SELECT SUM(count) FROM visits", BigDecimal.class)
                .getSingleResult();
        System.out.println("count: " + result.longValueExact());
        transaction.commit();
    }
}
