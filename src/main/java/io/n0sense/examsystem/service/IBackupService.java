package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.Backup;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface IBackupService {
    void dumpDatabase() throws IOException;
    void applyFromId(Long id) throws NoSuchElementException, IOException;
    void removeBackup(Long id) throws NoSuchElementException, IOException;
    List<Backup> findAll();
}
