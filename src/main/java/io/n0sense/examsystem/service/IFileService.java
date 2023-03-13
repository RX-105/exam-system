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

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

public interface IFileService {
    String saveFile(MultipartFile file, String subdir) throws IOException;
    String saveAvatar(MultipartFile file) throws IOException;

    Optional<FileInputStream> createTempFile(String filename) throws IOException;

    Optional<FileOutputStream> saveTempFile(String filename) throws FileNotFoundException;

    Resource getFile(String filename, String subdir) throws FileNotFoundException, MalformedURLException;
    Resource getAvatar(String filename) throws FileNotFoundException, MalformedURLException;

    Resource getTempFile(String filename) throws MalformedURLException, FileNotFoundException;
}
