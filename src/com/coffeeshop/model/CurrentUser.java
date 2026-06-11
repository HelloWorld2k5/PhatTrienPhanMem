package com.coffeeshop.model;

/*
 * Singleton Pattern:
 * Lưu 1 người dùng đang đăng nhập tại thời điểm hiện tại.
 */
public class CurrentUser {
    private static CurrentUser instance;
    private User user;

    private CurrentUser() {}

    public static synchronized CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void login(User user) {
        this.user = user;
    }

    public void logout() {
        this.user = null;
    }

    public boolean isLoggedIn() {
        return user != null;
    }
}