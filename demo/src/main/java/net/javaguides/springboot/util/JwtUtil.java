package net.javaguides.springboot.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Метод для генерации токена
    public String generateToken(String username, Long roleId) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role_id", roleId); // Добавляем ID роли в токен

        return Jwts.builder()
                .setClaims(claims) // Устанавливаем данные в токен
                .setIssuedAt(new Date(System.currentTimeMillis())) // Устанавливаем дату выдачи
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Время жизни токена (неделя)
                .signWith(SECRET_KEY) // Подписываем токен
                .compact(); // Создаем токен
    }


    // Метод для извлечения всех данных из токена
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    // Метод для извлечения имени пользователя из токена
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Метод для извлечения role_id из токена
    public Long extractRoleId(String token) {
        Claims claims = extractAllClaims(token);
        System.out.println(claims.get("role_id", Long.class));
        return claims.get("role_id", Long.class); // Извлекаем role_id
    }

    // Метод для проверки действительности токена
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Метод для проверки срока действия токена
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
