package com.victor.expensegate.service;

import com.victor.expensegate.entity.Authority;
import com.victor.expensegate.entity.Role;
import com.victor.expensegate.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class DbUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public DbUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        var authorities = new HashSet<GrantedAuthority>();

        user.getUserRoles()
                .stream()
                .map(Role::getName)
                .forEach(roleName ->
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase())));

        user.getUserRoles()
                .stream()
                .map(Role::getAuthorities)
                .flatMap(Collection::stream)
                .map(Authority::getName)
                .forEach(authorityName -> authorities.add(new SimpleGrantedAuthority(authorityName)));


        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
