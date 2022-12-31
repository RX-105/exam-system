package io.n0sense.examsystem.commons;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DecimalFormat;

@SpringBootTest
public class TestSystemStatistics {
    @Autowired
    private SystemStatistics statistics;
    @Test
    void getStat(){
        long free = (long) statistics.getStat("heap_free_memory");
        long total = (long) statistics.getStat("heap_total_memory");
        System.out.println("free: " + free);
        System.out.println("total: " + total);
        System.out.println("ratio: " + String.format("%.2f", (double)free/total * 100));
    }
}
