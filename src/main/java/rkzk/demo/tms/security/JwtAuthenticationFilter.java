//package rkzk.demo.tms.security;
//
//import java.io.IOException;
//import java.util.List;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//import rkzk.demo.tms.service.JwtService;
//
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String header = request.getHeader("Authorization"); // Извлекаем заголовок Authorization
//        if (header != null && header.startsWith("Bearer ")) { // Если присутствует заголовок и начинается с "Bearer "
//            String token = header.substring(7); // Удаляем "Bearer " и получаем сам токен
//            if (JwtService.validateToken(token)) { // Проверяем валидность токена
//                String username = JwtService.extractUserName(token); // Извлекаем имя пользователя из токена
//                List<String> roles = JwtService.extractRoles(token); // Извлекаем список ролей пользователя
//
//                // Создаем объект Authentication и устанавливаем его в контекст безопасности
//                UsernamePasswordAuthenticationToken auth
//                        = new UsernamePasswordAuthenticationToken(
//                                username,
//                        null,
//                        roles.stream().map(SimpleGrantedAuthority::new).toList());
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }
//        filterChain.doFilter(request, response); // Продолжаем обработку запроса
//    }
//
//
//}