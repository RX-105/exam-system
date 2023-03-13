/*
 * This file is part of exam-system.
 *
 * exam-system is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * exam-system is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with exam-system. If not, see <https://www.gnu.org/licenses/>.
 */

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
