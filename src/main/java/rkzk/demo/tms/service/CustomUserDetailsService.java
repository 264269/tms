package rkzk.demo.tms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.persistent.Role;
import rkzk.demo.tms.repository.CustomUserRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomUserRepository customUserRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        CustomUser myUser = customUserRepository
                .findByUsername(userName)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with username " + userName + " not found"));

        String[] roleArray = myUser.getRoles()
                .stream()
                .map(Role::getDescription)
                .toList()
                .toArray(new String[0]);

        UserDetails user = User.builder()
                .username(myUser.getUsername())
                .password(myUser.getPassword())
                .roles(roleArray)
                .build();

        return user;
    }
}