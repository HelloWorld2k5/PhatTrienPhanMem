package com.coffeeshop.controller;

import com.coffeeshop.model.User;
import com.coffeeshop.service.AuthService;

public class AuthController {
    private final AuthService authService = new AuthService();

    public User handleLogin(String username, String password) {
        return authService.login(username, password);
    }

    public void handleLogout() {
        authService.logout();
    }
}