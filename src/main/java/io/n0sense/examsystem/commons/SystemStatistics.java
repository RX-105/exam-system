package io.n0sense.examsystem.commons;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SystemStatistics {
    private final Map<String, Object> data = new HashMap<>();

    public SystemStatistics(){
        // 获取堆内存可用量
        Runtime instance = Runtime.getRuntime();
        final long MB_FACTOR = 1024L * 1024L;
        data.put("heap_total_memory", instance.totalMemory() / MB_FACTOR);
        data.put("heap_free_memory", instance.freeMemory() / MB_FACTOR);
        data.put("heap_used_memory", (instance.totalMemory()-instance.freeMemory()) / MB_FACTOR);
        data.put("heap_max_memory", instance.maxMemory() / MB_FACTOR);
    }

    public Object getStat(String name){
        return data.get(name);
    }
}
