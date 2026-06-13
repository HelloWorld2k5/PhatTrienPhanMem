package com.coffeeshop.factory;
import com.coffeeshop.view.LoginFrame;
import com.coffeeshop.view.MainFrame;
import java.awt.*;
import javax.swing.*;

public class StaffUIFactory implements UIFactory {
        // Nâu cà phê sáng (Ấm áp)
    private final Color COFFEE_BROWN = new Color(125, 95, 75);
    // Nâu sáng hơn khi di chuột
    private final Color COFFEE_HOVER = new Color(155, 125, 105);
    // Màu nền sidebar (Cream/Beige rất nhạt để làm nổi bật nút)
    private final Color SIDEBAR_BG = new Color(248, 244, 235);

    @Override
    public JPanel createSidebar(MainFrame mainFrame) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SIDEBAR_BG); // Màu nền sidebar
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15)); // Khoảng cách viền
        // Tạo các nút menu
        panel.add(createMenuButton("Thực đơn gọi món", e -> mainFrame.showPanel("MENU")));
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Khoảng cách giữa các nút

        panel.add(createMenuButton("Lịch sử hóa đơn", e -> mainFrame.showPanel("HISTORY")));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        // Đẩy nút Đăng xuất xuống dưới cùng
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Nút Đăng xuất (Vẫn là màu nâu để đồng bộ, hoặc có thể dùng màu xám đậm)
        JButton btnLogout = createMenuButton("Đăng xuất", e -> logout(mainFrame));
        btnLogout.setBackground(new Color(165, 42, 42)); // Nâu đỏ nhẹ để phân biệt
        panel.add(btnLogout);

        return panel;
    }

   private JButton createMenuButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Chiều cao cố định
        btn.setFont(new Font("Arial", Font.BOLD, 14));

        // Style mặc định
        btn.setBackground(COFFEE_BROWN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);

        // Thêm Action
        btn.addActionListener(action);

        // Hiệu ứng Hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(COFFEE_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(COFFEE_BROWN);
            }
        });

        return btn;
    }

    private void logout(MainFrame mainFrame) {
        int confirm = JOptionPane.showConfirmDialog(mainFrame,
                "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            mainFrame.dispose(); // Đóng cửa sổ hiện tại
            new LoginFrame().setVisible(true); // Mở lại form Login (Thay LoginFrame bằng tên class của bạn)
        }
    }

    @Override
    public String getDashboardTitle() {
        return "Coffee 175";
    }

    @Override
    public String getRole() {
        return "Nhân viên";
    }
}