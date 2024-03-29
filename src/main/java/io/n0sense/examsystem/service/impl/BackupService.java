/*
 * This file is part of exam-system.
 *
 * exam-system is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * exam-system is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with exam-system. If not, see <https://www.gnu.org/licenses/>.
 */

package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.annotation.RecordLog;
import io.n0sense.examsystem.commons.constants.Actions;
import io.n0sense.examsystem.entity.Backup;
import io.n0sense.examsystem.repository.BackupRepository;
import io.n0sense.examsystem.service.IBackupService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final BackupRepository backupRepository;
    private final HttpSession session;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${application.datasource.host}")
    private String host;
    @Value("${application.datasource.table-name}")
    private String tableName;
    @Value("${application.datasource.mysql-home}")
    private String mysqlHome;
    @Value("${application.datasource.backup-location}")
    private String backupLocation;

    @EventListener(ApplicationReadyEvent.class)
    public void validateConfiguration() {
        if (!new File(mysqlHome).exists()) {
            log.warning("MySQL安装路径不存在，请检查配置文件。");
        }
        File backupPath = new File(backupLocation);
        if (!backupPath.exists()) {
            if (backupPath.mkdirs()) {
                log.warning("无法访问备份文件保存路径，已创建该目录。");
            } else {
                log.warning("无法访问备份文件保存路径，且无法创建该目录。");
            }
        }
    }

    @Override
    @RecordLog(action = Actions.CREATE_BACKUP)
    public void dumpDatabase() throws IOException {
        String mysqlDumpPath = mysqlHome + File.separator + "mysqldump";
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
                " --result-file=" + backupLocation + File.separator + fileName +
                " " + tableName;
        Backup backup = Backup.builder()
                .filename(fileName)
                .time(now)
                .creator((String) session.getAttribute("username"))
                .build();
        backupRepository.save(backup);
        runtime.exec(command);
        log.info("将数据库导出到文件，命令行是：" + command);
    }

    @Override
    @RecordLog(action = Actions.RESTORE_BACKUP)
    public void applyFromId(Long id) throws NoSuchElementException, IOException {
        Backup backup = backupRepository.findById(id).orElseThrow();
        File backupFile = new File(backupLocation + File.separator + backup.getFilename());
        if (!backupFile.exists()) {
            throw new FileNotFoundException();
        }

        String mysqlPath = mysqlHome + File.separator + "mysql";
        String command = mysqlPath +
                " -h" + host +
                " -u" + username +
                " -p" + password +
                " " + tableName +
                " < " + backup.getFilename();
        runtime.exec(command);
        log.info("尝试还原数据库，备份ID为" + id + "，备份文件名为" + backup.getFilename());
    }

    @Override
    @RecordLog(action = Actions.REMOVE_BACKUP)
    public void removeBackup(Long id) throws NoSuchElementException, IOException {
        Backup backup = backupRepository.findById(id).orElseThrow();
        File backupFile = new File(backupLocation + File.separator + backup.getFilename());
        if (!backupFile.exists()) {
            backupRepository.deleteById(id);
            log.info("尝试移除一个备份记录，但文件不存在，ID为" + id + "，文件名为" + backup.getFilename());
            throw new FileNotFoundException();
        }
        if (backupFile.delete()) {
            backupRepository.deleteById(id);
            log.info("移除了一个备份记录，ID为" + id + "，文件名为" + backup.getFilename());
        } else {
            log.info("尝试移除一个备份文件，操作失败，ID为" + id + "，文件名为" + backup.getFilename());
            throw new IOException();
        }
    }

    @Override
    public Page<Backup> findAll(int page, int size) {
        return backupRepository.findAll(PageRequest.of(page, size));
    }
}
