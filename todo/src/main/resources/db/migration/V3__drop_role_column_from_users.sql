-- Xoá cột role khỏi bảng users vì đã chuẩn hoá role sang bảng roles + user_roles
ALTER TABLE users DROP COLUMN role;
