package com.coffeeshop.view;

import com.coffeeshop.controller.AuthController;
import com.coffeeshop.model.CurrentUser;
import com.coffeeshop.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginFrame extends JFrame {

    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JButton btnLogin = new JButton("ĐĂNG NHẬP");
    private final JLabel lblStatus = new JLabel(" ");

    private final AuthController authController = new AuthController();

    // Màu sắc
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19); // Coffee brown
    private static final Color SECONDARY_COLOR = new Color(210, 180, 140); // Tan
    private static final Color ACCENT_COLOR = new Color(255, 248, 220); // Cream
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color DARK_TEXT = new Color(80, 40, 20);

    public LoginFrame() {
        setTitle("Coffee Shop Management System - Đăng nhập");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(null);

        initUI();
        initEvents();
    }

    private void initUI() {
        JPanel root = new JPanel();
        root.setBackground(ACCENT_COLOR);
        root.setLayout(new BorderLayout());

        // Header panel with coffee theme
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel title = new JLabel("Coffee Shop");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Hệ Thống Quản Lý");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(SECONDARY_COLOR);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitle);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(ACCENT_COLOR);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        // Username field
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUsername.setForeground(DARK_TEXT);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblUsername);
        formPanel.add(Box.createVerticalStrut(5));

        txtUsername.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUsername.setBackground(Color.WHITE);
        txtUsername.setForeground(DARK_TEXT);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(txtUsername);
        formPanel.add(Box.createVerticalStrut(15));

        // Password field
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPassword.setForeground(DARK_TEXT);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblPassword);
        formPanel.add(Box.createVerticalStrut(5));

        txtPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setForeground(DARK_TEXT);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(txtPassword);
        formPanel.add(Box.createVerticalStrut(20));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(ACCENT_COLOR);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setBackground(PRIMARY_COLOR);
        btnLogin.setForeground(TEXT_COLOR);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setPreferredSize(new Dimension(120, 40));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(200, 100, 50));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(PRIMARY_COLOR);
            }
        });

        buttonPanel.add(btnLogin);
        buttonPanel.add(Box.createHorizontalGlue());

        formPanel.add(buttonPanel);

        // Status label
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(200, 0, 0));
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(lblStatus);

        // Add focus listeners
        addInputFocusListeners();

        root.add(headerPanel, BorderLayout.NORTH);
        root.add(formPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    private void addInputFocusListeners() {
        txtUsername.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtUsername.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 100, 50), 2),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtUsername.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)));
            }
        });

        txtPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtPassword.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 100, 50), 2),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtPassword.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)));
            }
        });
    }

    private void initEvents() {
        btnLogin.addActionListener(e -> doLogin());
        txtPassword.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        lblStatus.setText("");

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Vui lòng nhập tài khoản và mật khẩu");
            return;
        }

        try {
            User user = authController.handleLogin(username, password);

            if (user == null) {
                lblStatus.setText("Sai tài khoản, mật khẩu, hoặc tài khoản đã bị khóa");
                txtPassword.setText("");
                return;
            }

            CurrentUser.getInstance().login(user);

            JOptionPane.showMessageDialog(
                    this,
                    "Đăng nhập thành công!\nXin chào " + user.getFullName(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } catch (Exception e) {
            lblStatus.setText("Lỗi: " + e.getMessage());
        }
    }
}