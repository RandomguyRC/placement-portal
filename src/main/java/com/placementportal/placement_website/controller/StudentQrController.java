package com.placementportal.placement_website.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.placementportal.placement_website.model.Student;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/student")
public class StudentQrController {

    @GetMapping(value = "/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] generateStudentQr(HttpSession session) throws IOException {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            throw new RuntimeException("Not logged in");
        }

        // ✅ Encode full JSON object (exactly like working QR)
        String qrJson = String.format("{\"student_id\":\"%s\"}", student.getEnrollmentNumber());

        try {
            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWriter.encode(
                    qrJson,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();

        } catch (WriterException e) {
            throw new IOException("Failed to generate QR code", e);
        }
    }
}
