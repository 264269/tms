package rkzk.demo.tms.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rkzk.demo.tms.model.persistent.Role;
import rkzk.demo.tms.repository.RoleRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RoleInitializer {

    @Autowired
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        List<Role> roles = roleRepository.findAll();

        Map<Long, Role> roleMap = roles.stream()
                .collect(Collectors.toMap(Role::getRoleId, r -> r));

        Role.RoleEnum.initialize(roleMap);
    }
}
