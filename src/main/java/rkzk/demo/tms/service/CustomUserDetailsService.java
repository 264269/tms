//package rkzk.demo.tms.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import rkzk.demo.tms.model.CustomUser;
//import rkzk.demo.tms.model.persistent.Role;
//import rkzk.demo.tms.repository.UserRepository;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//    @Autowired
//    private MyUserRepository dao;
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        MyUser myUser= dao.findByLogin(userName);
//        if (myUser == null) {
//            throw new UsernameNotFoundException("Unknown user: "+userName);
//        }
//        UserDetails user = User.builder()
//                .username(myUser.getLogin())
//                .password(myUser.getPassword())
//                .roles(myUser.getRole())
//                .build();
//        return user;
//    }
//}