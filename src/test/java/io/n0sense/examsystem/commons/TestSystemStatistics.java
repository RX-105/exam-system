package io.n0sense.examsystem.commons;

import com.sun.management.OperatingSystemMXBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.management.ManagementFactory;

@SpringBootTest
public class TestSystemStatistics {
    @Autowired
    private SystemStatistics statistics;
    @Test
    void getStat(){
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpu = osBean.getCpuLoad();
        System.out.println("ratio: " + cpu);//String.format("%.2f", statistics.getStat("cpu_usage")));
    }
}
