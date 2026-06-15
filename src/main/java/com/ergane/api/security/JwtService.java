package com.ergane.api.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMinutes;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration-minutes}") long expirationMinutes) {

        this.secretKey = buildKey(secret);
        this.expirationMinutes = expirationMinutes;
    }

    private SecretKey buildKey(String secret) {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (Exception ex) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        // HS256 exige pelo menos 256 bits. Em vez de "esticar" um segredo curto
        // repetindo bytes (o que não adiciona entropia), falhamos na inicialização
        // para forçar a configuração de um segredo forte.
        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "security.jwt.secret deve ter no mínimo 32 bytes (256 bits). "
                            + "Configure um segredo aleatório forte via variável de ambiente JWT_SECRET.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String userId, String cpf, String nome) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId)
                .claim("cpf", cpf)
                .claim("nome", nome)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }
}
