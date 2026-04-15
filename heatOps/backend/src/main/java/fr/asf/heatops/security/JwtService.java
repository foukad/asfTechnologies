package fr.asf.heatops.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private static final String SECRET = "super-secret-key-heatops-should-be-very-long-256bits";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24h

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String email, UUID companyId) {
        return Jwts.builder()
                .subject(email)
                .claim("tenant",companyId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey()) // OK avec JJWT 0.12.x
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()                     // OK
                .verifyWith((SecretKey) getSigningKey())     // OK
                .build()
                .parseSignedClaims(token)        // OK
                .getPayload()
                .getSubject();
    }

    public String extractTenant(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("tenant", String.class);
    }

}
