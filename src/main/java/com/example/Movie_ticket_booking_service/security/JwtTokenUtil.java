package com.example.Movie_ticket_booking_service.security;

import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.*;

@Component
@Slf4j
public class JwtTokenUtil {
    private static final long EXPIRATION = 7 * 24 * 60 * 60 * 1000L; // in milliseconds

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    public String generateAccessToken(String email, String role) {
        try {

            byte[] secretBytes = Base64.getDecoder().decode(SIGNER_KEY.trim());
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .claim("role", role)
                    .issuer("Movie_ticket_book.com")
                    .issueTime(new Date(System.currentTimeMillis()))
                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION))
                    .jwtID(UUID.randomUUID().toString())
                    .build();

            Payload payload = new Payload(jwtClaimsSet.toJSONObject());

            JWSObject jwsObject = new JWSObject(header, payload);
            jwsObject.sign(new MACSigner(secretBytes));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token (JOSE): {}", e.getMessage(), e);
            throw new RuntimeException("Cannot create token", e);
        }
    }

    public JWTClaimsSet extractClaims(String token) throws JOSEException, ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet();
    }

    public boolean isExpired(String token){
        try {
            Date expirationTime = extractClaims(token).getExpirationTime();
            return expirationTime.before(new Date());
        }catch (ParseException | JOSEException exception){
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
    }
    public Instant extractExpirationTime(String token){
        try {
            Date expirationTime = extractClaims(token).getExpirationTime();
            return expirationTime.toInstant();
        }catch (ParseException | JOSEException exception){
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    public String extractEmail(String token){
        try {
            return extractClaims(token).getSubject();
        }catch (ParseException | JOSEException exception){
            throw new AppException(ErrorCode.INVALID_JWT_TOKEN);
        }
    }

    public String extractJwtId(String token){
        try {
            return extractClaims(token).getJWTID();
        }catch (ParseException | JOSEException exception){
            throw new AppException(ErrorCode.INVALID_JWT_TOKEN);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails){
        try {
            JWSVerifier verifier = new MACVerifier(Base64.getDecoder().decode(SIGNER_KEY.trim()));
            SignedJWT jwt = SignedJWT.parse(token);
            String email = extractEmail(token);
            boolean verified = jwt.verify(verifier);
            if (!verified) {
                log.warn("Invalid JWT signature for token: {}", token);
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            if (isExpired(token)) {
                log.warn("Token is expired: {}", token);
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            if (!email.equals(userDetails.getUsername())) {
                log.warn("email is incorrect: {}", token);
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            return true;
        } catch (JOSEException | ParseException e) {
            log.error("Token validation failed: {}", e.getMessage());
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
}
