package rkzk.demo.tms.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

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
    public enum RoleEnum {
        ADMIN(1L, "admin"),
        USER(2L, "user");

        private final Long roleId;
        private final String description;

        private Role role;

        RoleEnum(Long roleId, String description) {
            this.roleId = roleId;
            this.description = description;
        }

        public Role getRole() {
            if (role == null) {
                throw new IllegalStateException(
                        "RoleEnum not initialized. Ensure RoleEnum is initialized during application startup.");
            }
            return role;
        }

        public static Role getById(Long id) {
            return Arrays.stream(values())
                    .filter(r -> Objects.equals(r.roleId, id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No role with such ID"))
                    .getRole();
        }

        public static Role getByDescription(String description) {
            return Arrays.stream(values())
                    .filter(r -> Objects.equals(r.description, description))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No role with such description"))
                    .getRole();
        }

        public static void initialize(Map<Long, Role> roles) {
            for (Role.RoleEnum roleEnum : values()) {
                Role role = roles.get(roleEnum.roleId);
                if (role == null || !role.getDescription().equals(roleEnum.description)) {
                    throw new IllegalStateException(
                            String.format("RoleEnum mismatch for %s: DB contains invalid or missing value.",
                                    roleEnum.name()));
                }
                roleEnum.role = role;
            }
        }
    }
}
