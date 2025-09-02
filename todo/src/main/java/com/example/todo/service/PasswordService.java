package com.example.todo.service;

import com.example.todo.entity.PasswordResetToken;
import com.example.todo.entity.User;
import com.example.todo.repository.PasswordResetTokenRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordResetTokenRepository tokenRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Gửi mail reset mật khẩu
     */
    public void sendPasswordResetEmail(String email) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Không tìm thấy tài khoản với email này!");
        }

        User user = optionalUser.get();

        // Tạo token reset
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenRepo.save(token);

        // Build link reset (phải đúng /auth/reset-password)
        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token.getToken();

        // Soạn email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Đặt lại mật khẩu - TodoList");
        message.setText("Xin chào " + user.getUsername() + ",\n\n"
                + "Bạn đã yêu cầu đặt lại mật khẩu.\n"
                + "Vui lòng click vào link sau để đặt lại mật khẩu (có hiệu lực trong 30 phút):\n"
                + resetLink + "\n\n"
                + "Nếu bạn không yêu cầu, hãy bỏ qua email này.");

        // Gửi mail
        mailSender.send(message);
    }

    /**
     * Validate token reset
     */
    public PasswordResetToken validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetToken = tokenRepo.findByToken(token);
        if (resetToken.isEmpty()) {
            return null;
        }
        PasswordResetToken tokenEntity = resetToken.get();

        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepo.delete(tokenEntity); // xóa token hết hạn
            return null;
        }
        return tokenEntity;
    }

    /**
     * Reset mật khẩu
     */
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = validatePasswordResetToken(token);
        if (resetToken == null) {
            throw new RuntimeException("Token không hợp lệ hoặc đã hết hạn!");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        // Xóa token sau khi sử dụng
        tokenRepo.delete(resetToken);
    }
}
