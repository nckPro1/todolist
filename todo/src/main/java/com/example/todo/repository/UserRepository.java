package com.example.todo.repository;

import com.example.todo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm user theo username (dùng cho login)
    Optional<User> findByUsername(String username);

    // Tìm user theo email (dùng cho reset password)
    Optional<User> findByEmail(String email);

    // Kiểm tra tồn tại username
    boolean existsByUsername(String username);

    // Kiểm tra tồn tại email
    boolean existsByEmail(String email);
}
