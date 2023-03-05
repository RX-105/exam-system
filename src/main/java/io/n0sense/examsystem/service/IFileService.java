package io.n0sense.examsystem.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public interface IFileService {
    String saveFile(MultipartFile file, String subdir) throws IOException;
    String saveAvatar(MultipartFile file) throws IOException;
    Resource getFile(String filename, String subdir) throws FileNotFoundException, MalformedURLException;
    Resource getAvatar(String filename) throws FileNotFoundException, MalformedURLException;
}
