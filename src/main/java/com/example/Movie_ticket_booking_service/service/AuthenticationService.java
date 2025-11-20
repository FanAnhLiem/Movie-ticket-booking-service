package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.request.UserLoginDTO;
import com.example.Movie_ticket_booking_service.dto.request.TokenRequest;
import com.example.Movie_ticket_booking_service.dto.response.TokenResponse;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;

public interface AuthenticationService {
    TokenResponse login(UserLoginDTO userLoginDTO);
    String logout();
//    UserResponse

    @Service
    @Slf4j
    class TwoFactorService {
        private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
        private final QrGenerator qrGenerator = new ZxingPngQrGenerator();
        private final CodeGenerator codeGenerator = new DefaultCodeGenerator();
        private final TimeProvider timeProvider = new SystemTimeProvider();
        private final CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        public String generateNewSecret() {
            return secretGenerator.generate();
        }

        public String generateQrCodeImage(String secret, String username) {
            QrData data = new QrData.Builder()
                    .label(username)
                    .secret(secret)
                    .issuer("movie-booking") // Tên ứng dụng, có thể tùy chỉnh
                    .algorithm(HashingAlgorithm.SHA1)
                    .digits(6) // Độ dài mã OTP (6)
                    .period(30) // Thời gian sống của mã (30 giây)
                    .build();
            try {
                byte[] qrCodeBytes = qrGenerator.generate(data);
                return Base64.getEncoder().encodeToString(qrCodeBytes);
            } catch (QrGenerationException e) {
                log.error("Error generating QR code for user {}: {}", username, e.getMessage());
                throw new RuntimeException("Failed to generate QR code", e);
            }
        }

        public boolean verifyCode(String secret, String code) {
            return verifier.isValidCode(secret, code);
        }


    }
}
