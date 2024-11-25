package rkzk.demo.tms.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Role {
    @Id
    @Column(name = "role_id", nullable = false, unique = true)
    private Long roleId;

    @Column(nullable = false, unique = true, length = 20)
    private String description;

    @Getter
    @RequiredArgsConstructor
    public enum RoleEnum {
        ADMIN(new Role(1L, "admin")),
        USER(new Role(2L, "user"));

        private final Role role;
    }
}
