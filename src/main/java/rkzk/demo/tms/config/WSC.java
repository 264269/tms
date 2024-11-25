//package rkzk.demo.tms.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import rkzk.demo.tms.security.JwtAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    @Autowired
//    private JwtAuthenticationFilter jwtTokenFilter;
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
//                .authorities("ROLE_ADMIN")
//                .build());
//        return manager;
//    }
//
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////                .authorizeHttpRequests((requests) -> requests
////                        .requestMatchers("/admin/**").hasAuthority("user")
////                        .anyRequest().authenticated())
////                .formLogin(Customizer.withDefaults()); // Включаем форму входа
////
////        return http.build();
////    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement((a) -> a.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/api/admin/**").hasAnyRole("admin")
//                        .anyRequest().permitAll()) // Разрешаем все остальные запросы без авторизации
//                .httpBasic(Customizer.withDefaults()); // Включаем базовую HTTP-аутентификацию для доступа к /login
//
//        return http.build();
//    }
//
//    // Добавить бины для конфигурации
//    @Bean
//    public JwtAuthenticationFilter jwtTokenFilter() {
//        return new JwtAuthenticationFilter();
//    }
//}
