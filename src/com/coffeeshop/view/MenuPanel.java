package com.coffeeshop.view;

import com.coffeeshop.factory.AbstractMenuItemFactory;
import com.coffeeshop.factory.MenuItemFactoryRegistry;
import com.coffeeshop.model.MenuItem;
import com.coffeeshop.observer.CartSubject;
import com.coffeeshop.repository.MenuRepository;
import com.coffeeshop.util.MoneyUtil;
import com.coffeeshop.util.StatusUtil;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class MenuPanel extends JPanel {
    private MenuRepository menuRepository = new MenuRepository();
    private CartSubject cartSubject;

    private JTextField txtSearch;
    private JButton btnSearch;
    private JRadioButton rdoAll, rdoCoffee, rdoBakery, rdoCombo,rdoCoffeeMachine,rdoCoffeeTrad;
    private ButtonGroup filterGroup;
    private JButton btnAddNewItem;

    private JPanel pnlGridMenu;
    private String currentCategoryFilter = "ALL";

    // MÃ MÀU THEO THIẾT KẾ COFFEE BROWN PREMIUM SPECIFICATION
    private final Color COLOR_COFFEE_BROWN = new Color(0x5C, 0x40, 0x33); // Nâu cà phê đậm chủ đạo
    private final Color COLOR_WARM_CREAM = new Color(0xFF, 0xF8, 0xDC); // Nền Kem/Be ấm áp cao cấp
    private final Color COLOR_ESPRESSO_TEXT = new Color(0x3A, 0x2E, 0x1F); // Nâu Espresso siêu đậm cho chữ
    private final Color COLOR_LATTE_LIGHT = new Color(0x96, 0x79, 0x69); // Nâu Latte nhẹ cho nhãn phụ
    private final Color COLOR_WARM_COPPER = new Color(0xB8, 0x73, 0x33); // Đồng ấm sáng cho nút hành động chính

    private final Map<String, List<String>> filterMap = Map.of(
        "ALL", List.of("COFFEE_MACHINE", "COFFEE_TRADITIONAL", "BAKERY", "COMBO"),
        "COFFEE_MACHINE", List.of("COFFEE_MACHINE"),
        "COFFEE_TRADITIONAL", List.of("COFFEE_TRADITIONAL"),
        "BAKERY", List.of("BAKERY"),
        "COMBO", List.of("COMBO")
    );
    public MenuPanel(CartSubject cartSubject) {
        this.cartSubject = cartSubject;
        initUI();
        loadMenu();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_WARM_CREAM);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // =========================================================================
        // 1. THANH CÔNG CỤ PHÍA TRÊN (TÌM KIẾM, BỘ LỌC, NÚT THÊM MÓN)
        // =========================================================================
        JPanel pnlTopToolbar = new JPanel(new GridBagLayout());
        pnlTopToolbar.setBackground(COLOR_WARM_CREAM);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 8);
        gbc.fill = GridBagConstraints.VERTICAL;

        // 1.1 Khu vực Tìm Kiếm
        JLabel lblSearch = new JLabel("Tìm kiếm món: ");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 13));
        lblSearch.setForeground(COLOR_ESPRESSO_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        pnlTopToolbar.add(lblSearch, gbc);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(220, 32));
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 13));
        txtSearch.setBackground(Color.WHITE);
        txtSearch.setForeground(COLOR_ESPRESSO_TEXT);
        txtSearch.setBorder(BorderFactory.createLineBorder(COLOR_COFFEE_BROWN, 1));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlTopToolbar.add(txtSearch, gbc);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setPreferredSize(new Dimension(90, 32));
        btnSearch.setFont(new Font("Arial", Font.BOLD, 12));
        btnSearch.setFocusPainted(false);
        btnSearch.setBackground(COLOR_WARM_COPPER);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setBorder(null);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        pnlTopToolbar.add(btnSearch, gbc);

        // 1.2 Nút "+ THÊM MÓN VÀO THỰC ĐƠN"
        btnAddNewItem = new JButton("+ Thêm món vào thực đơn");
        btnAddNewItem.setBackground(new Color(40, 165, 90)); // Giữ màu xanh lá cho thao tác tạo mới an toàn
        btnAddNewItem.setForeground(Color.WHITE);
        btnAddNewItem.setFont(new Font("Arial", Font.BOLD, 12));
        btnAddNewItem.setFocusPainted(false);
        btnAddNewItem.setPreferredSize(new Dimension(180, 32));
        btnAddNewItem.setBorder(null);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 15, 5, 0);
        pnlTopToolbar.add(btnAddNewItem, gbc);

        // 1.3 Cụm Nút Bộ Lọc Danh Mục (Theo phong cách thanh tab trên
        // edited-image_2.png)
        JPanel pnlRadioFilters = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        pnlRadioFilters.setBackground(COLOR_WARM_CREAM);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_LATTE_LIGHT, 1),
                " DANH MỤC THỰC ĐƠN ", 0, 0,
                new Font("Arial", Font.BOLD | Font.ITALIC, 11));
        titledBorder.setTitleColor(COLOR_COFFEE_BROWN);
        pnlRadioFilters.setBorder(titledBorder);

        // rdoAll = new JRadioButton("Tất cả các món", true);
        // rdoCoffee = new JRadioButton("Hương vị Cà phê");
        // rdoBakery = new JRadioButton("Bánh bông & Bánh kẹp");
        // rdoCombo = new JRadioButton("Combo tiết kiệm");
        rdoAll = new JRadioButton("Tất cả", true);
        rdoCoffeeMachine = new JRadioButton("Cà phê máy");
        rdoCoffeeTrad = new JRadioButton("Cà phê truyền thống");
        rdoBakery = new JRadioButton("Bánh ngọt");
        rdoCombo = new JRadioButton("Combo");

        // Đồng bộ Font chữ & Màu sắc cho các nút Radio để đạt tương phản cao
        Font radioFont = new Font("Arial", Font.BOLD, 12);
        for (JRadioButton rdo : new JRadioButton[] { rdoAll, rdoCoffeeMachine,rdoCoffeeTrad, rdoBakery, rdoCombo }) {
            rdo.setFont(radioFont);
            rdo.setBackground(COLOR_WARM_CREAM);
            rdo.setForeground(COLOR_ESPRESSO_TEXT);
            rdo.setFocusPainted(false);
        }

        filterGroup = new ButtonGroup();
        filterGroup.add(rdoAll);
        filterGroup.add(rdoCoffeeMachine);
        filterGroup.add(rdoCoffeeTrad);
        filterGroup.add(rdoBakery);
        filterGroup.add(rdoCombo);

        pnlRadioFilters.add(rdoAll);
        pnlRadioFilters.add(rdoCoffeeMachine);
        pnlRadioFilters.add(rdoCoffeeTrad);
        pnlRadioFilters.add(rdoBakery);
        pnlRadioFilters.add(rdoCombo);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        pnlTopToolbar.add(pnlRadioFilters, gbc);

        add(pnlTopToolbar, BorderLayout.NORTH);

        // =========================================================================
        // 2. KHU VỰC LƯỚI SẢN PHẨM KHÔNG BỊ CO GIÃN SAI LỆCH (FIXED GRID WRAPPER)
        // =========================================================================
        pnlGridMenu = new JPanel(new GridLayout(0, 3, 15, 15));
        pnlGridMenu.setBackground(COLOR_WARM_CREAM);

        // Neo lưới sản phẩm lên phía Bắc để tránh tình trạng các Card bị dãn rộng thô
        // kệch
        JPanel pnlGridNorthAnchor = new JPanel(new BorderLayout());
        pnlGridNorthAnchor.setBackground(COLOR_WARM_CREAM);
        pnlGridNorthAnchor.add(pnlGridMenu, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(pnlGridNorthAnchor);
        scrollPane.setBorder(null);
        scrollPane.setBackground(COLOR_WARM_CREAM);
        scrollPane.getViewport().setBackground(COLOR_WARM_CREAM);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================================
        // 3. ĐĂNG KÝ LISTENERS ĐIỀU KHIỂN BỘ LỌC
        // =========================================================================
        rdoAll.addActionListener(e -> { currentCategoryFilter = "ALL"; loadMenu(); });
        rdoCoffeeMachine.addActionListener(e -> { currentCategoryFilter = "COFFEE_MACHINE"; loadMenu(); });
        rdoCoffeeTrad.addActionListener(e -> { currentCategoryFilter = "COFFEE_TRADITIONAL"; loadMenu(); });
        rdoBakery.addActionListener(e -> { currentCategoryFilter = "BAKERY"; loadMenu(); });
        rdoCombo.addActionListener(e -> { currentCategoryFilter = "COMBO"; loadMenu(); });
        btnSearch.addActionListener(e -> loadMenu());
        txtSearch.addActionListener(e -> loadMenu());
        btnAddNewItem.addActionListener(e -> openAddNewItemDialog());
    }

    private void loadMenu() {
        pnlGridMenu.removeAll();

        List<MenuItem> items = menuRepository.getAllMenuItems();
        String searchKeyword = txtSearch.getText().trim().toLowerCase();

        for (MenuItem item : items) {
            if (item == null || !StatusUtil.isItemAvailable(item.getStatus())) {
                // continue;
            }

            if (!searchKeyword.isEmpty() && !item.getName().toLowerCase().contains(searchKeyword)) {
                continue;
            }

            String category = item.getCategory() != null ? item.getCategory().toUpperCase() : "";
            if (!"ALL".equals(currentCategoryFilter)) {
                List<String> validCategories = filterMap.get(currentCategoryFilter);
                if (validCategories == null || !validCategories.contains(category)) {
                    continue;
                }
            }

            // 1. TẠO THẺ PANEL CHỨA SẢN PHẨM (Card có viền Nâu Cà Phê, Nền Trắng Tinh Tế)
            JPanel pnlItemCard = new JPanel(new BorderLayout(0, 8));
            pnlItemCard.setPreferredSize(new Dimension(160, 180));
            pnlItemCard.setBackground(Color.WHITE);
            pnlItemCard.setBorder(BorderFactory.createLineBorder(COLOR_COFFEE_BROWN, 1, true));

            // 2. PHẦN ĐỈNH CARD: Thể loại nhãn nhỏ góc trên (Ví dụ: CÀ PHÊ, BÁNH NGỌT)
            JPanel pnlCardHeader = new JPanel(new BorderLayout());
            pnlCardHeader.setBackground(Color.WHITE);
            pnlCardHeader.setBorder(new EmptyBorder(6, 8, 0, 8));

            JLabel lblTag = new JLabel(category.replace("COFFEE_", ""));
            lblTag.setFont(new Font("Arial", Font.BOLD, 10));
            lblTag.setForeground(COLOR_LATTE_LIGHT);
            pnlCardHeader.add(lblTag, BorderLayout.WEST);

            // MỚI: Nhãn Trạng thái (Góc phải)
            String status = item.getStatus();
            boolean isAvailable = "AVAILABLE".equalsIgnoreCase(status);
            JLabel lblStatus = new JLabel(isAvailable ? "● Sẵn sàng" : "● Hết hàng");
            lblStatus.setFont(new Font("Arial", Font.BOLD, 9));
            lblStatus.setForeground(isAvailable ? new Color(40, 165, 90) : new Color(217, 83, 79));
            pnlCardHeader.add(lblStatus, BorderLayout.EAST);

            pnlItemCard.add(pnlCardHeader, BorderLayout.NORTH);
            // 3. THÂN CARD CHỨA NÚT HIỂN THỊ THÔNG TIN MÓN CHÍNH
            String itemHTML = String.format(
                    "<html><center>"
                            + "<font size='6'>%s</font><br><br>"
                            + "<span style='font-family:Arial; font-size:12px; font-weight:bold;'>%s</span><br>"
                            + "<span style='font-family:Arial; font-size:11px; color:#967969;'>%s</span>"
                            + "</center></html>",
                    item.getIcon() != null ? item.getIcon() : "☕",
                    item.getName(),
                    MoneyUtil.format(item.getBasePrice()));

            JButton btnItemMain = new JButton(itemHTML);
            btnItemMain.setBackground(Color.WHITE);
            btnItemMain.setBorder(null);
            btnItemMain.setFocusPainted(false);
            btnItemMain.setToolTipText(item.getDescription());
            btnItemMain.setHorizontalAlignment(SwingConstants.CENTER);

            // Sự kiện click chọn món đưa vào giỏ hàng
            btnItemMain.addActionListener(e -> {
                Window parentWindow = SwingUtilities.getWindowAncestor(this);
                if (parentWindow instanceof Frame) {
                    DrinkOptionDialog dialog = new DrinkOptionDialog((Frame) parentWindow, item, cartSubject);
                    dialog.setVisible(true);
                }
            });
            pnlItemCard.add(btnItemMain, BorderLayout.CENTER);

            // 4. ĐÁY THẺ CARD: KHU VỰC THAO TÁC (3 nút Sửa, Xóa, Chọn món thẳng hàng ngang
            // hoàn hảo)
            JPanel pnlCardFooter = new JPanel(new GridLayout(1, 3, 4, 0));
            pnlCardFooter.setBackground(Color.WHITE);
            pnlCardFooter.setBorder(new EmptyBorder(0, 6, 6, 6));

            JButton btnEdit = new JButton("Sửa");
            btnEdit.setFont(new Font("Arial", Font.PLAIN, 11));
            btnEdit.setFocusPainted(false);
            btnEdit.setBackground(COLOR_WARM_CREAM);
            btnEdit.setForeground(COLOR_COFFEE_BROWN);
            btnEdit.setBorder(BorderFactory.createLineBorder(COLOR_LATTE_LIGHT, 1));
            btnEdit.addActionListener(e -> openEditItemDialog(item));

            JButton btnDelete = new JButton("Xóa");
            btnDelete.setFont(new Font("Arial", Font.PLAIN, 11));
            btnDelete.setFocusPainted(false);
            btnDelete.setBackground(COLOR_WARM_CREAM);
            btnDelete.setForeground(new Color(217, 83, 79)); // Đỏ dịu cảnh báo
            btnDelete.setBorder(BorderFactory.createLineBorder(COLOR_LATTE_LIGHT, 1));

            btnDelete.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this, "Bạn có chắc chắn muốn xóa món '" + item.getName() + "' không?",
                        "Xác nhận hệ thống", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    item.setStatus("DELETED");
                    menuRepository.deleteMenuItem(item.getId());
                    JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái xóa thành công.");
                    loadMenu();
                }
            });

            JButton btnSelect = new JButton("Chọn");
            btnSelect.setFont(new Font("Arial", Font.BOLD, 11));
            btnSelect.setFocusPainted(false);
            btnSelect.setBackground(COLOR_WARM_COPPER); // Nút Chọn món có màu Đồng ấm tạo điểm nhấn
            btnSelect.setForeground(Color.WHITE);
            btnSelect.setBorder(null);
            // btnSelect.addActionListener(btnItemMain.getActionListeners()[0]); // Chia sẻ
            // chung logic mở Dialog chọn món

            // ✅ Tạo 1 hàm chung
            btnSelect.addActionListener(e -> {
                Window parentWindow = SwingUtilities.getWindowAncestor(this);
                if (parentWindow instanceof Frame) {
                    DrinkOptionDialog dialog = new DrinkOptionDialog(
                            (Frame) parentWindow, item, cartSubject);
                    dialog.setVisible(true);
                }
            });

            // Đưa 3 nút vào lưới hàng ngang dưới đáy
            pnlCardFooter.add(btnEdit);
            pnlCardFooter.add(btnDelete);
            pnlCardFooter.add(btnSelect);

            pnlItemCard.add(pnlCardFooter, BorderLayout.SOUTH);

            // Bổ sung thẻ sản phẩm hoàn chỉnh vào lưới tổng
            pnlGridMenu.add(pnlItemCard);
        }

        pnlGridMenu.revalidate();
        pnlGridMenu.repaint();
    }

    private void openEditItemDialog(MenuItem item) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Món Ăn", true);
        dialog.setLayout(new GridLayout(7, 2, 10, 10)); // Tăng lên 7 dòng để thêm nút Hủy nếu cần
        ((JPanel) dialog.getContentPane()).setBackground(COLOR_WARM_CREAM);
        ((JPanel) dialog.getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        // Khởi tạo các trường nhập liệu với dữ liệu hiện tại của item
        JTextField txtName = new JTextField(item.getName());
        JTextField txtPrice = new JTextField(String.format("%.0f", item.getBasePrice()));
        JTextField txtDesc = new JTextField(item.getDescription());
        JTextField txtIcon = new JTextField(item.getIcon());

        JComboBox<String> cbCategory = new JComboBox<>(
                new String[] { "COFFEE_MACHINE", "COFFEE_TRADITIONAL", "BAKERY", "COMBO" });
        cbCategory.setSelectedItem(item.getCategory());

        JComboBox<String> cbStatus = new JComboBox<>(new String[] { "AVAILABLE", "OUT_OF_STOCK" });
        cbStatus.setSelectedItem(item.getStatus());

        // Thêm các thành phần vào Dialog
        dialog.add(new JLabel("Tên món:"));
        dialog.add(txtName);
        dialog.add(new JLabel("Danh mục:"));
        dialog.add(cbCategory);
        dialog.add(new JLabel("Giá (đ):"));
        dialog.add(txtPrice);
        dialog.add(new JLabel("Mô tả:"));
        dialog.add(txtDesc);
        dialog.add(new JLabel("Icon:"));
        dialog.add(txtIcon);
        dialog.add(new JLabel("Trạng thái:"));
        dialog.add(cbStatus);

        JButton btnSave = new JButton("Lưu thay đổi");
        btnSave.setBackground(COLOR_WARM_COPPER);
        btnSave.setForeground(Color.WHITE);

        btnSave.addActionListener(e -> {
            try {
                // 1. Cập nhật dữ liệu vào object 'item'
                item.setName(txtName.getText().trim());
                item.setCategory((String) cbCategory.getSelectedItem());
                item.setBasePrice(Double.parseDouble(txtPrice.getText().trim()));
                item.setDescription(txtDesc.getText().trim());
                item.setIcon(txtIcon.getText().trim());
                item.setStatus((String) cbStatus.getSelectedItem());

                // 2. Gọi Repository để lưu vào Database
                if (menuRepository.updateMenuItem(item)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                    dialog.dispose(); // Đóng cửa sổ
                    loadMenu(); // Tải lại giao diện menu
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi cập nhật database!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá tiền phải là số!");
            }
        });

        dialog.add(new JLabel()); // Ô trống cho căn lề
        dialog.add(btnSave);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openAddNewItemDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Món Ăn Mới", true);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        ((JPanel) dialog.getContentPane()).setBackground(COLOR_WARM_CREAM);
        ((JPanel) dialog.getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField txtName = new JTextField();
        JComboBox<String> cbCategory = new JComboBox<>(new String[] {
                "COFFEE_MACHINE", "COFFEE_TRADITIONAL", "BAKERY", "COMBO"
        });
        JTextField txtPrice = new JTextField("0");
        JTextField txtIcon = new JTextField("☕");
        JTextField txtDesc = new JTextField();

        Font lblFont = new Font("Arial", Font.BOLD, 12);
        JLabel l1 = new JLabel("Tên món ăn (*):");
        l1.setFont(lblFont);
        l1.setForeground(COLOR_ESPRESSO_TEXT);
        JLabel l2 = new JLabel("Danh mục món:");
        l2.setFont(lblFont);
        l2.setForeground(COLOR_ESPRESSO_TEXT);
        JLabel l3 = new JLabel("Giá cơ bản (đ):");
        l3.setFont(lblFont);
        l3.setForeground(COLOR_ESPRESSO_TEXT);
        JLabel l4 = new JLabel("Biểu tượng (Icon):");
        l4.setFont(lblFont);
        l4.setForeground(COLOR_ESPRESSO_TEXT);
        JLabel l5 = new JLabel("Mô tả ngắn:");
        l5.setFont(lblFont);
        l5.setForeground(COLOR_ESPRESSO_TEXT);

        dialog.add(l1);
        dialog.add(txtName);
        dialog.add(l2);
        dialog.add(cbCategory);
        dialog.add(l3);
        dialog.add(txtPrice);
        dialog.add(l4);
        dialog.add(txtIcon);
        dialog.add(l5);
        dialog.add(txtDesc);

        JButton btnSave = new JButton("Lưu món mới");
        btnSave.setBackground(COLOR_COFFEE_BROWN);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 12));
        btnSave.setFocusPainted(false);
        btnSave.setBorder(null);

        btnSave.addActionListener(e -> {
            String name = txtName.getText().trim();
            if (name.isEmpty())
                return;

            try {
                double basePrice = Double.parseDouble(txtPrice.getText().trim());
                String category = (String) cbCategory.getSelectedItem();
                String icon = txtIcon.getText().trim().isEmpty() ? "☕" : txtIcon.getText().trim();
                String desc = txtDesc.getText().trim();
                String status = "AVAILABLE";
                AbstractMenuItemFactory factory = MenuItemFactoryRegistry.getFactory(category);
            if (factory != null) {
                MenuItem newItem = factory.createMenuItem(0, name, category, basePrice, desc, icon, status);
            if (menuRepository.insertNewMenuItem(newItem)) {
                dialog.dispose();
                loadMenu();
                }
            }
            } catch (NumberFormatException ex) {
            }
        });

        dialog.add(new JLabel());
        dialog.add(btnSave);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}