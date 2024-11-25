package rkzk.demo.tms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rkzk.demo.tms.model.persistent.Role;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CustomUser {
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @Column(name = "enabled")
    private boolean enabled;

//    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private List<Task> ownedTasks = new ArrayList<>();

//    @OneToMany(mappedBy = "executor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private List<Task> assignedTasks = new ArrayList<>();

//    public User(SignUpRequest signUpRequest) {
//        this.username = signUpRequest.username();
//        this.email = signUpRequest.email();
//        this.password = signUpRequest.password();
//        this.role = new Role(RoleEnum.USER);
//    }
}