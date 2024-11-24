package rkzk.demo.tms.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
    ADMIN("administrator", 1L),
    USER("dummy", 2L);

    private final String description;
    private final Long id;

    public static RoleEnum fromDescription(String description) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.description.equalsIgnoreCase(description)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + description);
    }

    public static RoleEnum fromRole(Role role) {
        return fromDescription(role.getDescription());
    }
}
