package com.example.todo.service;

import com.example.todo.entity.Role;
import com.example.todo.entity.User;
import com.example.todo.repository.RoleRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Đăng ký user mới (mặc định gán role USER_BASIC)
     */
    public User registerUser(String username, String email, String rawPassword) {
        // Kiểm tra username/email đã tồn tại chưa
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Tạo user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);

        // Gán role USER_BASIC mặc định
        Role defaultRole = roleRepository.findByName("USER_BASIC")
                .orElseThrow(() -> new RuntimeException("Default role USER_BASIC not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    /**
     * Nâng cấp user thành USER_PRO
     */
    public User upgradeToPro(Long userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optUser.get();

        Role proRole = roleRepository.findByName("USER_PRO")
                .orElseThrow(() -> new RuntimeException("Role USER_PRO not found"));

        user.getRoles().add(proRole);
        return userRepository.save(user);
    }

    /**
     * Gán role ADMIN cho user
     */
    public User makeAdmin(Long userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optUser.get();

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

        user.getRoles().add(adminRole);
        return userRepository.save(user);
    }
}
