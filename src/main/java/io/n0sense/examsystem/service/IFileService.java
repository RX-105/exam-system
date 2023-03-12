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
