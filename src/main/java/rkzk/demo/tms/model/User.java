package rkzk.demo.tms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rkzk.demo.tms.data.SignUpRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "user_password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

//    public User(SignUpRequest signUpRequest) {
//        this.username = signUpRequest.username();
//        this.email = signUpRequest.email();
//        this.password = signUpRequest.password();
//        this.role = new Role(RoleEnum.USER);
//    }

    public void setRole(RoleEnum roleEnum) {
        this.role = new Role(roleEnum);
    }

    public boolean checkPassword(String rawPassword) {
        return rawPassword.equals(this.password);
    }
}