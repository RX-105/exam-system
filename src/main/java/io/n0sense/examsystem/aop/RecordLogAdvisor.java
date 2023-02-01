package io.n0sense.examsystem.aop;

import io.n0sense.examsystem.annotation.RecordLog;
import io.n0sense.examsystem.service.impl.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RecordLogAdvisor {
    private final HttpServletRequest request;
    private final LogService logService;

    @After(value = "@annotation(recordLog)", argNames = "recordLog")
    public void recordNewLog(RecordLog recordLog) {
        logService.record(recordLog.action(), recordLog.message(), request);
    }
}
