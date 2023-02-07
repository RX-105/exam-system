package io.n0sense.examsystem.service;

import io.n0sense.examsystem.service.impl.BackupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@SpringBootTest
@Transactional
public class TestBackupService {
    @Autowired
    BackupService backupService;
    @Test
    void testDump(){
        try {
            backupService.dumpDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void testRestore(){
        try{
            backupService.applyFromId(100010L);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
