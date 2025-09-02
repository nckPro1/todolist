-- Tạo bảng roles
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) UNIQUE NOT NULL
);

-- Tạo bảng user_roles (quan hệ N-N)
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY(user_id, role_id),
                            FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY(role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Tạo index để tối ưu join/truy vấn
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);

-- Insert role mặc định
INSERT INTO roles(name) VALUES
                            ('ADMIN'),
                            ('USER_BASIC'),
                            ('USER_PRO');
