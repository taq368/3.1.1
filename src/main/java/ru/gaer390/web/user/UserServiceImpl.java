package ru.gaer390.web.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gaer390.web.role.Role;
import ru.gaer390.web.role.RoleServiceImpl;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        log.info("User found in the database: {}", email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isEnabled(),
                user.isEnabled(),
                user.isEnabled(),
                user.getAuthorities());
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        log.info("Fetching user");
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User with id " + id + " does not exists"));
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Fetching user by Email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Delete user from database");
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("User with id " + id + " does not exists");
        }
        userRepository.deleteById(id);
    }

    @Override
    public void addNewUser(User user) {
        log.info("Saving new user {} to database", user.getEmail());
        User userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail != null) {
            throw new IllegalStateException("Email taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        log.info("Update user {} to database", user.getEmail());
        User userWillBeUpdate = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("User with id " + user.getId() + " does not exists"));
        if (!userWillBeUpdate.getEmail().equals(user.getEmail())) {
            userWillBeUpdate.setEmail(user.getEmail());
        }
        if (!userWillBeUpdate.getFirst_name().equals(user.getFirst_name())) {
            userWillBeUpdate.setFirst_name(user.getFirst_name());
        }
        if (!userWillBeUpdate.getLast_name().equals(user.getLast_name())) {
            userWillBeUpdate.setLast_name(user.getLast_name());
        }
        if (!userWillBeUpdate.getDob().equals(user.getDob())) {
            userWillBeUpdate.setDob(user.getDob());
        }
        if (!userWillBeUpdate.getPassword().equals(user.getPassword())) {
            userWillBeUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (userWillBeUpdate.isEnabled() != user.isEnabled()) {
            userWillBeUpdate.setEnabled(user.isEnabled());
        }
        userWillBeUpdate.setRoles(new HashSet<Role>());
    }

    @Override
    public void addRoleToUser(String email, String nameRole) {
        log.info("Adding role {} to user {}", email, nameRole);
        User user = userRepository.findByEmail(email);
        Role role = roleService.findByNameRole(nameRole);
        user.getRoles().add(role);
    }

}
