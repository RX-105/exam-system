package io.n0sense.examsystem.interceptor;

import io.n0sense.examsystem.entity.Visits;
import io.n0sense.examsystem.repository.VisitsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StatisticsInterceptor implements HandlerInterceptor {
    private final VisitsRepository visitsRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Optional<Visits> optionalVisits = visitsRepository.findById(LocalDate.now());
        Visits visits;
        if (optionalVisits.isPresent()){
            visits = optionalVisits.get();
            visits.setCount(visits.getCount() + 1);
        } else {
            visits = new Visits(LocalDate.now(), 1L);
        }
        visitsRepository.save(visits);
        return true;
    }
}
