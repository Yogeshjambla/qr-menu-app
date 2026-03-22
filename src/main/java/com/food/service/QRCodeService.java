package com.food.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Service
public class QRCodeService {

    @Value("${app.qr.code.directory:./qrcodes}")
    private String qrCodeDirectory;

    @Value("${app.base.url:http://localhost:8080}")
    private String baseUrl;

    public String generateQRCode(String text, String fileName) throws WriterException, IOException {
        int width = 300;
        int height = 300;
        
        BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        
        Path path = FileSystems.getDefault().getPath(qrCodeDirectory, fileName + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        
        return baseUrl + "/qrcodes/" + fileName + ".png";
    }

    public String generateMenuQRCode(Long tableId) {
        String menuUrl = baseUrl + "/menu?table=" + tableId;
        String fileName = "menu_table_" + tableId;
        
        try {
            return generateQRCode(menuUrl, fileName);
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}
