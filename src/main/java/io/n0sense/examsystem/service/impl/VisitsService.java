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

package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.entity.Visits;
import io.n0sense.examsystem.repository.VisitsRepository;
import io.n0sense.examsystem.service.IVisitsService;
import io.n0sense.examsystem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class VisitsService implements IVisitsService {
    private final VisitsRepository visitsRepository;
    private final Session session;

    @Autowired
    public VisitsService(HibernateUtil hibernateUtil, VisitsRepository visitsRepository){
        this.session = hibernateUtil.getSessionJavaConfigFactory().openSession();
        this.visitsRepository = visitsRepository;
    }

    @Override
    public Long getTodayVisits() {
        Optional<Visits> optionalTodayVisit = visitsRepository.findById(LocalDate.now());
        Visits todayVisit = optionalTodayVisit.orElse(new Visits(LocalDate.now(), 1L));
        return todayVisit.getCount();
    }

    @Override
    public Long getTotalVisits() {
        Transaction transaction = session.beginTransaction();
        BigDecimal totalVisit =
                session.createNativeQuery("select sum(count) from visits", BigDecimal.class)
                                .getSingleResult();
        transaction.commit();
        return totalVisit.longValue();
    }

    @Override
    public Long getTotalUsers() {
        Transaction transaction = session.beginTransaction();
        BigInteger totalUser =
                session.createNativeQuery("select count(*) from visits", BigInteger.class)
                        .getSingleResult();
        transaction.commit();
        return totalUser.longValue();
    }
}
