//package rkzk.demo.tms.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import rkzk.demo.tms.model.CustomUser;
//import rkzk.demo.tms.model.persistent.Role;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//
//public class JwtService {
//    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
//    private static final long EXPIRATION_TIME = 86400000; // Время жизни токена (в миллисекундах)
//
//    public static String extractUserName(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    @SuppressWarnings("unchecked")
//    public static List<String> extractRoles(String token) {
//        List<String> roles;
//        Claims claims = extractAllClaims(token);
//        roles = (List<String>) claims.get("roles");
//        return roles;
//    }
//
//    public static Boolean isUserInRole(String token, String role) {
//        return extractRoles(token).contains(role);
//    }
//
//    public static String generateToken(Authentication authentication) {
//        String username = authentication.getName();
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
//
//
//
//        return Jwts.builder()
//                .subject(username)
//                .claim("roles", authentication.getAuthorities())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SECRET_KEY, Jwts.SIG.HS256)
//                .compact();
//    }
//
//    public static String generateToken(CustomUser userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        {
//            claims.put("userId", userDetails.getUserId());
//            claims.put("email", userDetails.getEmail());
//            claims.put("role",
//                    userDetails
//                            .getRoles()
//                            .stream()
//                            .map(Role::getDescription)
//                            .toList()
//            );
//        }
////        if (userDetails instanceof User customUserDetails) {
////            claims.put("userId", customUserDetails.getUserId());
////            claims.put("email", customUserDetails.getEmail());
////            claims.put("role", customUserDetails.getRole());
////        }
//        return generateToken(claims, userDetails);
//    }
//
//    public static boolean validateToken(String token) {
//        try {
//            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }
//    }
//
//    public static boolean isTokenValid(String token, UserDetails userDetails) {
//        final String userName = extractUserName(token);
//        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
//    }
//
//    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolvers.apply(claims);
//    }
//
//    private static String generateToken(Map<String, Object> extraClaims, CustomUser user) {
//        return Jwts.builder()
//                .subject(user.getUsername())
//                .claims(extraClaims)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SECRET_KEY, Jwts.SIG.HS256)
//                .compact();
//    }
//
//    private static boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private static Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    private static Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .verifyWith(SECRET_KEY)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//}
