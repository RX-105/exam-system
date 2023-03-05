package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.service.IFileService;
import io.n0sense.examsystem.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

@Service
@Log
@RequiredArgsConstructor
public class FileService implements IFileService {
    @Value("${application.app-data-location}")
    private String appDataPath;

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
        String path = appDataPath + subdir + File.separator + filename;
        file.transferTo(new File(path));
        return filename;
    }

    @Override
    public String saveAvatar(MultipartFile file) throws IOException {
        return saveFile(file, File.separator + "avatars");
    }

    @Override
    public Resource getFile(String filename, String subdir) throws FileNotFoundException, MalformedURLException {
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
}
