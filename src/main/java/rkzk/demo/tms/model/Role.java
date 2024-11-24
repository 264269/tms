package rkzk.demo.tms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Role {
    @Id
    @Column(name = "role_id", nullable = false, unique = true)
    private Long roleId;

    @Column(nullable = false, unique = true, length = 20)
    private String description;

    protected Role() {}

    private Role(Long roleId, String description) {
        this.roleId = roleId;
        this.description = description;
    }

    public Role(RoleEnum roleEnum) {
        this(roleEnum.getId(), roleEnum.getDescription());
    }

    /**
     * Запрет изменений (роль неизменяема)
     * @param roleId
     * @throws IllegalAccessException
     */
    public void setRoleId(Long roleId) throws IllegalAccessException {
        if (this.roleId != 0)
            throw new IllegalAccessException(); // to prevent changes, id never 0 in db
        this.roleId = roleId;
    }

    /**
     * Запрет изменений (роль неизменяема)
     * @param description
     * @throws IllegalAccessException
     */
    public void setDescription(String description) throws IllegalAccessException {
        if (this.description != null)
            throw new IllegalAccessException();
        this.description = description;
    }
}
