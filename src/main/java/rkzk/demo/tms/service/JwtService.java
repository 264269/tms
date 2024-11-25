package rkzk.demo.tms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.model.CustomUser;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final SecretKey jwtSigningKey = Jwts.SIG.HS256.key().build();
    private static final long EXPIRATION_TIME = 86400000; // Время жизни токена (в миллисекундах)

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлечение данных и генерация токена
     *
     * @param userDetails данные пользователя
     * @return токен
     */
    public String generateToken(CustomUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        {
            claims.put("userId", userDetails.getUserId());
            claims.put("email", userDetails.getEmail());
            claims.put("role", userDetails.getRoles());
        }
//        if (userDetails instanceof User customUserDetails) {
//            claims.put("userId", customUserDetails.getUserId());
//            claims.put("email", customUserDetails.getEmail());
//            claims.put("role", customUserDetails.getRole());
//        }
        return generateToken(claims, userDetails);
    }

    /**
     * Проверка токена на валидность
     *
     * @param token       токен
     * @param userDetails данные пользователя
     * @return true, если токен валиден
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Извлечение данных из токена
     *
     * @param token           токен
     * @param claimsResolvers функция извлечения данных
     * @param <T>             тип данных
     * @return данные
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Генерация токена
     *
     * @param extraClaims дополнительные данные
     * @param user данные пользователя
     * @return токен
     */
    private String generateToken(Map<String, Object> extraClaims, /*UserDetails*/ CustomUser user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claims(extraClaims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(jwtSigningKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Проверка токена на просроченность
     *
     * @param token токен
     * @return true, если токен просрочен
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлечение даты истечения токена
     *
     * @param token токен
     * @return дата истечения
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtSigningKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
