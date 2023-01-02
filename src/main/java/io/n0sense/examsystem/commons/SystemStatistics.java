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
    private static Runtime instance;
    private static OperatingSystemMXBean osBean;
    private static File root;
    private static long MB_FACTOR = 1024L * 1024L;
    private static long GB_FACTOR = 1024L * 1024L * 1024L;

    public SystemStatistics() {
        // 获取堆内存数据
        instance = Runtime.getRuntime();
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
        osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        data.put("system_free_memory", osBean.getFreeMemorySize() / MB_FACTOR);
        data.put("system_total_memory", osBean.getTotalMemorySize() / MB_FACTOR);
        data.put("system_memory_usage",
                100 -
                        (double) (osBean.getFreeMemorySize() / MB_FACTOR)
                                /
                                (osBean.getTotalMemorySize() / MB_FACTOR)
                                * 100
        );

        // 获取系统CPU负载
        data.put("cpu_usage", 100 * osBean.getCpuLoad());

        // 获取系统分区可用空间
        String os = System.getProperty("os.name");
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


    /**
     * 根据属性名获取对应的系统属性。
     * @param name 属性名称
     * @return 属性对应的值
     * @deprecated
     * 这种方法获取的系统属性值不是实时更新的，而是Spring IoC容器创建时一次性收集的值，不能反映当前系统的状态，
     * 所以不应该使用使用这个方法获取系统属性。只有在获取固定不变的系统属性时才可以使用这个方法（比如处理器数量、
     * 系统架构、已安装的内存等等），或者是对于性能要求高的场景。<br/>
     * 你应该使用对应的静态方法，比如对于获取堆内存占用率，使用{@link #getHeapUsage()}，而不是给本方法提供
     * "heap_usage"参数。
     */
    @Deprecated
    public Object getStat(String name) {
        return data.get(name);
    }

    public static double getHeapUsage(){
        return 100 -
                (double) (instance.freeMemory() / MB_FACTOR)
                        /
                        (double) (instance.totalMemory() / MB_FACTOR)
                        * 100;
    }

    public static double getSystemMemoryUsage(){
        return 100 -
                (double) (osBean.getFreeMemorySize() / MB_FACTOR)
                        /
                        (double) (osBean.getTotalMemorySize() / MB_FACTOR)
                        * 100;
    }

    public static double getCPUUsage(){
        return 100 * osBean.getCpuLoad();
    }

    public static double getRootDiskUsage(){
        return 100 -
                (double) (root.getFreeSpace() / GB_FACTOR)
                        /
                        (double) (root.getTotalSpace() / GB_FACTOR)
                        * 100;
    }
}
