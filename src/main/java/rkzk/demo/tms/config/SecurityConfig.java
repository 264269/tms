package rkzk.demo.tms.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter<HttpSecurity> {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().authenticated()
//                );
////                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/task_management"); // замените 'your_database_name' на имя вашей базы данных
//        dataSource.setUsername("postgres"); // ваше имя пользователя для подключения к БД
//        dataSource.setPassword("postgres"); // ваш пароль для подключения
//
//        return dataSource;
//    }
//
//    @Bean
//    public UserDetailsManager users() {
//        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
//
//        if (!manager.userExists("user")) { // Проверяем существование пользователя перед созданием
//            UserDetails user = User.withDefaultPasswordEncoder()
//                    .username("user")
//                    .password("password")
//                    .roles("USER")
//                    .build();
//
//            manager.createUser(user); // Создаем нового пользователя
//        }
//
//        return manager;
//    }
//}

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, user_password, enabled FROM users WHERE username=?")
                .authoritiesByUsernameQuery("SELECT u.username, r.description " +
                        "FROM users u " +
                        "INNER JOIN authorities a ON u.user_id = a.user_id " +
                        "INNER JOIN roles r ON a.role_id = r.role_id " +
                        "WHERE u.username=?");
    }
}
