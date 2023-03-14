package io.n0sense.examsystem.service;

import com.google.zxing.WriterException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public interface IGraphicsService {
    Optional<FileOutputStream> renderEAN8Code(String text, StringBuilder fileName) throws WriterException, IOException;

    Optional<FileOutputStream> renderQRCode(String text, StringBuilder fileName) throws WriterException, IOException;
}
