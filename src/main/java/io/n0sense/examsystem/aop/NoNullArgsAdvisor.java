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

import io.n0sense.examsystem.annotation.NoNullArgs;
import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.entity.R;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.*;

@Aspect
@Component
@Log
public class NoNullArgsAdvisor {
    @SuppressWarnings("rawtypes")
    @AfterReturning(value = "@annotation(annotation)", returning = "result",
            argNames = "joinPoint,result,annotation")
    public void doCheck(JoinPoint joinPoint, R result, NoNullArgs annotation) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        List<String> methodArgNames = Arrays.stream(signature.getParameterNames()).toList();
        List<String> annotatedArgNames = Arrays.stream(annotation.excludedNames()).distinct().toList();

        List<Class> methodClasses = Arrays.stream(signature.getParameterTypes()).toList();
        List<Class> annotatedClasses = Arrays.stream(annotation.excludedTypes()).distinct().toList();

        Object[] args = joinPoint.getArgs();
        Set<Integer> excludedIndexes = new HashSet<>();
        // 遍历annotatedArgNames，获得排除元素的下标
        if (!annotatedArgNames.isEmpty()) {
            for (String arg : annotatedArgNames) {
                excludedIndexes.add(methodArgNames.indexOf(arg));
            }
        }
        // 遍历annotatedClassNames，获得排除元素的下标。类类型可能重复。
        if (!annotatedClasses.isEmpty()) {
            for (Class arg : annotatedClasses) {
                for (int i = 0; i < methodClasses.size(); i++) {
                    if (methodClasses.get(i) == arg) {
                        excludedIndexes.add(i);
                    }
                }
            }
        }
        excludedIndexes.remove(-1);
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null && !excludedIndexes.contains(i)) {
                result.setStatus(Status.ERR_PARAMETER_NOT_PRESENT);
                result.setMessage("参数不完整。");
                HashMap<String, Object> data = (HashMap<String, Object>) result.getData();
                if (data == null) {
                    data = new HashMap<>();
                }
                data.put("location", "/404");
                result.setData(data);
                break;
            }
        }
    }
}
