package io.n0sense.examsystem.service;

import java.io.IOException;
import java.util.NoSuchElementException;

public interface IBackupService {
    void dumpDatabase() throws IOException;
    void restoreDBFromId(Long id) throws NoSuchElementException, IOException;
}
