//package rkzk.demo.tms.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.lang.NonNull;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import rkzk.demo.tms.model.CustomUser;
//
//import java.io.IOException;
//import java.util.Objects;
//
//@Component
//public class TaskAccessFilter extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain) throws ServletException, IOException {
//
//        String path = request.getServletPath();
//
//        if (!path.startsWith("/tasks/")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth instanceof AnonymousAuthenticationToken || auth == null) {
//            response.setStatus(403);
//            return;
//        }
//
//        CustomUser user = (CustomUser) auth.getPrincipal();
//        Long paramUserId = Long.parseLong(path.split("/")[2]);
//
//        if (Objects.equals(user.getUserId(), paramUserId)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        for (GrantedAuthority authority : auth.getAuthorities()) {
//            if (authority.getAuthority().equals("admin")) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//        }
//
//        response.setStatus(403);
//    }
//}