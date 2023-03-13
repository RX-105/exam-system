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

import io.n0sense.examsystem.config.properties.ApplicationProperties;
import io.n0sense.examsystem.service.IFileService;
import io.n0sense.examsystem.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Optional;

@Service
@Log
@RequiredArgsConstructor
public class FileService implements IFileService {
    private final ApplicationProperties applicationProperties;

    @Override
    public String saveFile(MultipartFile file, String subdir) throws IOException {
        if (!StringUtils.hasLength(file.getOriginalFilename())
                || file.getOriginalFilename().startsWith(".")) {
            throw new InvalidFileNameException(file.getOriginalFilename(), "不可以使用这个文件名。");
        }
        String filename = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."))
                + "-"
                + PasswordEncoder.MD5Encrypt(file.getOriginalFilename()).substring(0,6)
                + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String path = applicationProperties.getAppDataLocation() + subdir + File.separator + filename;
        file.transferTo(new File(path));
        return filename;
    }

    @Override
    public String saveAvatar(MultipartFile file) throws IOException {
        return saveFile(file, File.separator + "avatars");
    }

    /**
     * 在程序临时目录下创建一个文件，命名为给定参数。返回一个文件输入流，用于对创建的文件进行IO操作。
     * @param filename 要创建的文件的文件名。
     * @return 一个Optional对象。如果文件不存在且成功创建文件，则该对象包含一个文件IO流；
     * 如果文件已经存在，则返回空Optional对象，而不是删除文件。
     * @throws IOException 如果无法创建文件，或是出现其他IO异常，将会抛出这个错误。
     */
    @Override
    public Optional<FileInputStream> createTempFile(String filename) throws IOException {
        File subdir = new File(applicationProperties.getAppDataLocation()
                + File.separator + "temp");
        if (!subdir.exists()) {
            if (!subdir.mkdirs()) {
                return Optional.empty();
            }
        }
        String path = applicationProperties.getAppDataLocation()
                + File.separator + "temp" + File.separator + filename;
        File temp = new File(path);
        if (!temp.exists()) {
            if (!temp.createNewFile()) {
                throw new IOException("无法创建文件。");
            } else {
                return Optional.of(new FileInputStream(temp));
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * 创建给定文件名的文件输出流。
     * @param filename 文件名
     * @return 一个Optional对象。如果参数指向一个合法文件，则Optional中包含一个文件输出流，否则为空Optional对象。
     * @throws FileNotFoundException 表示给定文件名的文件不存在
     */
    @Override
    public Optional<FileOutputStream> saveTempFile(String filename) throws FileNotFoundException {
        File subdir = new File(applicationProperties.getAppDataLocation() + File.separator + "temp");
        if (!subdir.exists()) {
            if (!subdir.mkdirs()) {
                return Optional.empty();
            }
        }
        String path = applicationProperties.getAppDataLocation()
                + File.separator + "temp" + File.separator + filename;
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            return Optional.empty();
        }
        return Optional.of(new FileOutputStream(file));
    }

    @Override
    public Resource getFile(String filename, String subdir) throws FileNotFoundException, MalformedURLException {
        String appDataPath = applicationProperties.getAppDataLocation();
        File file = new File(appDataPath + subdir + File.separator + filename);
        if (!file.exists() || file.isDirectory()) {
            log.warning(appDataPath + subdir + File.separator + filename + " not found");
            throw new FileNotFoundException();
        }
        return new UrlResource(file.toURI());
    }

    @Override
    public Resource getAvatar(String filename) throws FileNotFoundException, MalformedURLException {
        return getFile(filename, File.separator + "avatars");
    }

    @Override
    public Resource getTempFile(String filename) throws MalformedURLException, FileNotFoundException {
        return getFile(filename, File.separator + "temp");
    }
}
