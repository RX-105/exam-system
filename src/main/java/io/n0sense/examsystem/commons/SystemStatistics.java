package io.n0sense.examsystem.commons;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

@Component
public class SystemStatistics {
    private final Map<String, Object> data = new HashMap<>();

    public SystemStatistics() {
        final long MB_FACTOR = 1024L * 1024L;
        final long GB_FACTOR = 1024L * 1024L * 1024L;

        // 获取堆内存数据
        Runtime instance = Runtime.getRuntime();
        data.put("heap_total_memory", instance.totalMemory() / MB_FACTOR);
        data.put("heap_free_memory", instance.freeMemory() / MB_FACTOR);
        data.put("heap_used_memory", (instance.totalMemory() - instance.freeMemory()) / MB_FACTOR);
        data.put("heap_max_memory", instance.maxMemory() / MB_FACTOR);
        data.put("heap_usage",
                100 -
                        (double) (instance.freeMemory() / MB_FACTOR)
                                /
                                (instance.totalMemory() / MB_FACTOR)
                                * 100
        );

        // 获取系统内存数据
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        data.put("system_free_memory", osBean.getFreeMemorySize() / MB_FACTOR);
        data.put("system_total_memory", osBean.getTotalMemorySize() / MB_FACTOR);
        data.put("system_memory_usage",
                100 -
                        (double) (osBean.getFreeMemorySize() / MB_FACTOR)
                                /
                                (double) (osBean.getTotalMemorySize() / MB_FACTOR)
                                * 100
        );

        // 获取系统CPU负载
        data.put("cpu_usage", osBean.getCpuLoad());

        // 获取系统分区可用空间
        String os = System.getProperty("os.name");
        File root;
        if (os.toLowerCase().contains("windows")) {
            root = new File("C:\\");
        } else {
            root = new File("/");
        }
        data.put("disk_available_space", root.getFreeSpace());
        data.put("disk_total_space", root.getTotalSpace() / GB_FACTOR);
        data.put("disk_usage",
                100 -
                        (double) (root.getFreeSpace() / GB_FACTOR)
                                /
                                (root.getTotalSpace() / GB_FACTOR)
                                * 100
        );
    }

    public Object getStat(String name){
        return data.get(name);
    }
}
