package rkzk.demo.tms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import rkzk.demo.tms.service.CustomUserDetailsService;

//@EnableWebSecurity(debug = true)
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService userDetailsService;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http.authorizeHttpRequests(authorize ->
////                authorize.anyRequest().);
//        return http.build();
//    }

//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/admin/**").hasRole( "ADMIN")
//                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/**").permitAll()
//                .and().formLogin();
//    }
//
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }
}