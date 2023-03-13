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

package io.n0sense.examsystem.aop;

import io.n0sense.examsystem.annotation.RecordLog;
import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.entity.R;
import io.n0sense.examsystem.service.impl.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class RecordLogAdvisor {
    private final HttpServletRequest request;
    private final LogService logService;

    @SuppressWarnings("rawtypes")
    @AfterReturning(value = "@annotation(recordLog)", returning = "result", argNames = "joinPoint,recordLog,result")
    public void recordNewLog(JoinPoint joinPoint, RecordLog recordLog, Object result) {
        Signature signature = joinPoint.getSignature();
        Class returnType = ((MethodSignature) signature).getReturnType();
        if (returnType == int.class || returnType == Integer.class) {
            try {
                if (((int) result) == Status.OK) {
                    doRecord(recordLog.action(), recordLog.message());
                }
            } catch (ClassCastException ignored) {
            }
        } else if (returnType == R.class) {
            try {
                if (((R) result).getStatus() == Status.OK) {
                    doRecord(recordLog.action(), recordLog.message());
                }
            } catch (ClassCastException ignored) {
            }
        } else {
            doRecord(recordLog.action(), recordLog.message());
        }
    }

    protected void doRecord(String[] actions, String[] messages) {
        if (messages.length < actions.length) {
            int oldLength = messages.length;
            messages = Arrays.copyOf(messages, actions.length);
            Arrays.fill(messages, oldLength, oldLength + 1, "");
        }
        for (int i = 0; i < actions.length; i++) {
            logService.record(actions[i], messages[i], request);
        }
    }
}
