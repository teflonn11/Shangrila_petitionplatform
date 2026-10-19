package com.example.shangrilapp.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
public class QRCodeScanner {
    private final BioIdValidator bioIdValidator;

    public QRCodeScanner(BioIdValidator bioIdValidator) {
        this.bioIdValidator = bioIdValidator;
    }

    public String scanQRCode(MultipartFile qrCodeFile) throws IOException {
        try {
            BufferedImage bufferedImage = ImageIO.read(qrCodeFile.getInputStream());
            if (bufferedImage == null) {
                throw new IOException("Could not read image file");
            }

            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            com.google.zxing.Result result = new MultiFormatReader().decode(bitmap);
            String scannedText = result.getText();

            // Validate the scanned BioID
            if (bioIdValidator.isValid(scannedText)) {
                return scannedText;
            } else {
                throw new IllegalArgumentException("Invalid BioID from QR code");
            }

        } catch (NotFoundException e) {
            throw new IllegalArgumentException("No QR code found in the image");
        } catch (ReaderException e) {
            throw new IllegalArgumentException("Error reading QR code");
        }
    }

    public String scanQRCodeFromBase64(String base64Image) throws IOException {
        // Implementation for handling base64 encoded images
        // This would be useful for web-based QR code scanning
        throw new UnsupportedOperationException("Base64 QR code scanning not implemented yet");
    }

    private boolean validateScanResult(String result) {
        return result != null && bioIdValidator.isValid(result);
    }
}