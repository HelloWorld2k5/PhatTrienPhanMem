package com.coffeeshop.service;

import com.coffeeshop.model.CurrentUser;
import com.coffeeshop.model.User;
import com.coffeeshop.model.UserStatus;
import com.coffeeshop.repository.UserRepository;
import com.coffeeshop.util.PasswordUtil;

public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    /**
     * Validate login input
     */
    private void validateLoginInput(String username, String rawPassword) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập tên người dùng!");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập mật khẩu!");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("Tên người dùng phải có ít nhất 3 ký tự!");
        }
        if (rawPassword.length() < 1) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 1 kí tự!");
        }
    }

    public User login(String username, String rawPassword) {
        try {
            validateLoginInput(username, rawPassword);

            User user = userRepository.findByUsername(username);
            if (user == null) {
                System.err.println("Đăng nhập thất bại: Tài khoản không tồn tại! " + username);
                return null;
            }

            if (user.getUserStatus() != UserStatus.ACTIVE) {
                System.err.println("Đăng nhập thất bại: Tài khoản chưa được kích hoạt! " + username);
                return null;
            }

            String hash = PasswordUtil.sha256(rawPassword);
            if (!hash.equals(user.getEncryptedPassword())) {
                System.err.println("Đăng nhập thất bại: Mật khẩu chính xác! " + username);
                return null;
            }

            CurrentUser.getInstance().login(user);
            System.out.println("Đăng nhập thành công: " + username);
            return user;
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi xác thực: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Lỗi xảy ra khi đăng nhập: " + e.getMessage());
            throw new RuntimeException("Đăng nhập thất bại:", e);
        }
    }

    public void logout() {
        try {
            CurrentUser.getInstance().logout();
            System.out.println("Đăng xuất thành công!");
        } catch (Exception e) {
            System.err.println("Lỗi xảy ra khi đăng xuất: " + e.getMessage());
            throw new RuntimeException("Đăng xuất thất bại: ", e);
        }
    }
}