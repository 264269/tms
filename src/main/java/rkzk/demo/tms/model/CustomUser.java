package rkzk.demo.tms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rkzk.demo.tms.model.persistent.Role;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "accountNonExpired", "credentialsNonExpired", "accountNonLocked"})
public class CustomUser implements UserDetails {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "user_password", nullable = false)
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();

    @Column(name = "enabled")
    @JsonIgnore
    private boolean enabled;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Task> ownedTasks = new ArrayList<>();

    @OneToMany(mappedBy = "executor", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Task> assignedTasks = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities;
        authorities =
                roles
                        .stream()
                        .map(role
                                -> new SimpleGrantedAuthority(role.getDescription()))
                        .toList();
        return authorities;
    }

    public boolean isAdmin() {
        for (GrantedAuthority authority : getAuthorities()) {
            if (Objects.equals(
                    authority.getAuthority(),
                    Role.RoleEnum.ADMIN.getRole().getDescription()))
                return true;
        }
        return false;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @PreRemove
    public void preRemove() {
        dismissAllOwned();
        dismissAllExecuting();
    }

    public void dismissAllOwned() {
//        this.ownedTasks.forEach(Task::dismissOwner);
        if (this.ownedTasks == null) return;
        List<Task> ownedTasksCopy = new ArrayList<>(this.ownedTasks);
        for (Task task : ownedTasksCopy) {
            this.ownedTasks.remove(task);
        }
        this.ownedTasks.clear();
    }

    public void dismissOwned(Task task) {
        this.ownedTasks.remove(task);
    }

    public void dismissAllExecuting() {
//        this.assignedTasks.forEach(Task::dismissExecutor);
        if (this.assignedTasks == null) return;
        List<Task> assignedTasksCopy = new ArrayList<>(this.assignedTasks);
        for (Task task : assignedTasksCopy) {
            this.assignedTasks.remove(task);
        }
        this.assignedTasks.clear();
    }

    public void dismissExecuting(Task task) {
        this.assignedTasks.remove(task);
    }
}