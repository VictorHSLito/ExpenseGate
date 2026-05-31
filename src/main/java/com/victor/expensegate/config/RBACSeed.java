package com.victor.expensegate.config;

import com.victor.expensegate.entity.Authority;
import com.victor.expensegate.entity.Role;
import com.victor.expensegate.entity.User;
import com.victor.expensegate.enums.Department;
import com.victor.expensegate.repository.AuthorityRepository;
import com.victor.expensegate.repository.RoleRepository;
import com.victor.expensegate.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static com.victor.expensegate.entity.Authority.Values.*;

@Configuration
public class RBACSeed implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(RBACSeed.class);

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RBACSeed(
            AuthorityRepository authorityRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("RBAC SEED STARTED");

        Authority expCreate = ensureAuthority(EXP_CREATE);
        Authority expRead = ensureAuthority(EXP_READ);
        Authority expReadAny = ensureAuthority(EXP_READ_ANY);
        Authority expApprove = ensureAuthority(EXP_APPROVE);
        Authority expApproveAny = ensureAuthority(EXP_APPROVE_ANY);

        Role roleEmployee = ensureRole("EMPLOYEE", Set.of(expCreate, expRead));
        Role roleManager = ensureRole("MANAGER", Set.of(expCreate, expRead, expApprove));
        Role roleAdmin = ensureRole("ADMIN", Set.of(expCreate, expRead, expReadAny, expApprove, expApproveAny));

        User luana = ensureUser("luana.lua", "password123", Department.IT, Set.of(roleEmployee));
        User carlos = ensureUser("carlos.c", "password123", Department.ENG, Set.of(roleManager));
        User mateus = ensureUser("mateus.m", "password123", Department.IT, Set.of(roleManager));
        User victor = ensureUser("victor.lito", "securityPassword123", Department.IT, Set.of(roleAdmin));
        logger.info("RBAC SEED ENDED");
    }

    public Authority ensureAuthority(String name) {
        return authorityRepository.findByName(name)
                .orElseGet(() -> authorityRepository.save(new Authority(null, name)));
    }

    public Role ensureRole(String name, Set<Authority> authoritySet) {
        return roleRepository.findByName(name)
                .map(existingRole -> {
                    existingRole.setAuthorities(authoritySet);
                    return roleRepository.save(existingRole);
                })
                .orElseGet(() -> roleRepository.save(new Role(null, name, authoritySet)));
    }

    public User ensureUser(String username, String password, Department department, Set<Role> userRoles) {
        return userRepository.findByUsername(username).map(
                existingUser -> {
                    existingUser.setPassword(passwordEncoder.encode(password));
                    existingUser.setDepartment(department);
                    existingUser.setUserRoles(userRoles);
                    return userRepository.save(existingUser);
                }
        ).orElseGet(() -> userRepository.save(new User(
                null,
                username,
                passwordEncoder.encode(password),
                department,
                userRoles
        )));
    }
}
