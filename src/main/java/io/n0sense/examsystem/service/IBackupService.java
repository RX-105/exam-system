package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.Backup;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.NoSuchElementException;

public interface IBackupService {
    void dumpDatabase() throws IOException;
    void applyFromId(Long id) throws NoSuchElementException, IOException;
    void removeBackup(Long id) throws NoSuchElementException, IOException;
    Page<Backup> findAll(int page, int size);
}
