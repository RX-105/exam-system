package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.annotation.RecordLog;
import io.n0sense.examsystem.commons.Actions;
import io.n0sense.examsystem.commons.CommonConstants;
import io.n0sense.examsystem.entity.Backup;
import io.n0sense.examsystem.repository.BackupRepository;
import io.n0sense.examsystem.service.IBackupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Log
public class BackupService implements IBackupService {
    private final Runtime runtime = Runtime.getRuntime();
    @Autowired
    private final BackupRepository backupRepository;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.host}")
    private String host;
    private final String tableName = CommonConstants.TABLE_NAME;
    private final String mysqlHome = CommonConstants.MYSQL_HOME;

    @Override
    @RecordLog(action = Actions.CREATE_BACKUP)
    public void dumpDatabase() throws IOException {
        String mysqlDumpPath = mysqlHome + "\\mysqldump";
        LocalDateTime now = LocalDateTime.now();
        String fileName = tableName +
                "-" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) +
                ".sql";
        String command = mysqlDumpPath +
                " -h" + host +
                " -u" + username +
                " -p" + password +
                " --default-character-set=utf8" +
                " --hex-blob" +
                " --result-file=.\\" + fileName +
                " " + tableName;
        Backup backup = Backup.builder()
                .filename(fileName)
                .time(now)
                .build();
        backupRepository.save(backup);
        runtime.exec(command);
        log.info("将数据库导出到文件，命令行是：" + command);
    }

    @Override
    @RecordLog(action = Actions.RESTORE_BACKUP)
    public void restoreDBFromId(Long id) throws NoSuchElementException, IOException {
        Backup backup = backupRepository.findById(id).orElseThrow();
        if (!new File(backup.getFilename()).exists()){
            throw new FileNotFoundException();
        }

        String mysqlPath = mysqlHome + "\\mysql";
        String command = mysqlPath +
                " -h" + host +
                " -u" + username +
                " -p" + password +
                " " + tableName +
                " < " + backup.getFilename();
        runtime.exec(command);
    }
}
