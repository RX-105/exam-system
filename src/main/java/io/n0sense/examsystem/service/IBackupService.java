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
