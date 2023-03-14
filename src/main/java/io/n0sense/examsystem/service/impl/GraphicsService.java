package io.n0sense.examsystem.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import io.n0sense.examsystem.service.IGraphicsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GraphicsService implements IGraphicsService {
    private final FileService fileService;

    @Override
    public Optional<FileOutputStream> renderEAN8Code(String text, StringBuilder fileName) throws WriterException, IOException {
        EAN8Writer barcodeWriter = new EAN8Writer();
        BitMatrix matrix = barcodeWriter.encode(text, BarcodeFormat.EAN_8, 300, 150);
        return render(matrix, text, fileName, BarcodeFormat.EAN_8.name());
    }

    @Override
    public Optional<FileOutputStream> renderQRCode(String text, StringBuilder fileName) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        return render(bitMatrix, text, fileName, BarcodeFormat.QR_CODE.name());
    }

    private Optional<FileOutputStream> render(BitMatrix bitMatrix, String text, StringBuilder fileName, String format) throws IOException {
        Optional<FileOutputStream> fos = Optional.empty();
        int counter = 0;
        while (fos.isEmpty()) {
            fileName.setLength(0);
            fileName.append("code-").append(text).append(counter).append(".jpg");
            fos = fileService.writeTempFile(fileName.toString());
            counter++;
            if (counter > 100) {
                return Optional.empty();
            }
        }
        MatrixToImageWriter.writeToStream(bitMatrix, format, fos.get());
        return fos;
    }
}
