package io.n0sense.examsystem.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;

public class BarcodeUtil {
    public static BufferedImage renderBarcode(String text) throws WriterException {
        EAN8Writer barcodeWriter = new EAN8Writer();
        BitMatrix matrix = barcodeWriter.encode(text, BarcodeFormat.EAN_8, 300, 150);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    public static BufferedImage renderQRCode(String text) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
