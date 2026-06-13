package com.coffeeshop.view;

import com.coffeeshop.config.AppConfig;
import com.coffeeshop.model.CurrentUser;
import com.coffeeshop.model.Invoice;
import com.coffeeshop.model.InvoiceItem;
import com.coffeeshop.model.TableItem;
import com.coffeeshop.observer.CartSubject;
import com.coffeeshop.observer.Observer;
import com.coffeeshop.observer.Subject;
import com.coffeeshop.service.PaymentService;
import com.coffeeshop.util.DateTimeUtil;
import com.coffeeshop.util.InvoiceIdGenerator;
import com.coffeeshop.util.MoneyUtil;
import com.coffeeshop.util.StatusUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CartPanel extends JPanel implements Observer {
    private CartSubject cartSubject;
    private JComboBox<TableItem> cbTables;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel lblSubTotal, lblTax, lblTotal;
    private JButton btnCheckout, btnClearAll, btnUndo, btnRedo, btnCopyOrder;

    // Lưu tạm danh sách trạng thái bàn để phục vụ logic sao chép đơn
    private List<TableItem> listTablesMemory = new ArrayList<>();

    // BẢNG MÀU ĐỒNG BỘ THEO STYLE COFFEE BROWN PREMIUM (TƯƠNG ĐỒNG MENUPANEL)
    private final Color COLOR_COFFEE_BROWN = new Color(0x5C, 0x40, 0x33); // Nâu cà phê đậm chủ đạo
    private final Color COLOR_WARM_CREAM = new Color(0xFF, 0xF8, 0xDC); // Nền Kem/Be ấm áp cao cấp
    private final Color COLOR_ESPRESSO_TEXT = new Color(0x3A, 0x2E, 0x1F); // Nâu Espresso sẫm cho chữ
    private final Color COLOR_LATTE_LIGHT = new Color(0x96, 0x79, 0x69); // Nâu Latte nhẹ cho nhãn phụ
    private final Color COLOR_WARM_COPPER = new Color(0xB8, 0x73, 0x33); // Đồng ấm cho nút hành động chính

    public CartPanel(CartSubject cartSubject) {
        this.cartSubject = cartSubject;
        this.cartSubject.registerObserver(this);
        initUI();
        loadTablesFromDatabase();

    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setBackground(COLOR_WARM_CREAM); // Thay màu nền xám cũ bằng màu Kem Be ấm áp

        // --- 1. PHẦN TRÊN: CHỌN BÀN ĂN & NÚT XÓA SẠCH ---
        JPanel pnlTopMain = new JPanel(new GridBagLayout());
        pnlTopMain.setBackground(COLOR_WARM_CREAM);
        pnlTopMain.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 5);
        gbc.fill = GridBagConstraints.VERTICAL;

        // 1.1 Nhãn "Lựa chọn bàn:"
        JLabel lblSelectTable = new JLabel("Lựa chọn bàn: ");
        lblSelectTable.setFont(new Font("Arial", Font.BOLD, 12));
        lblSelectTable.setForeground(COLOR_ESPRESSO_TEXT); // Chữ màu nâu đậm hạt Espresso
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        pnlTopMain.add(lblSelectTable, gbc);

        // 1.2 Hộp chọn JComboBox bàn ăn
        cbTables = new JComboBox<>();
        cbTables.setPreferredSize(new Dimension(150, 30));
        cbTables.setFont(new Font("Arial", Font.BOLD, 12));
        cbTables.setBackground(Color.WHITE); // Giữ nền trắng dễ đọc thông tin
        cbTables.setForeground(COLOR_ESPRESSO_TEXT);
        cbTables.setBorder(BorderFactory.createLineBorder(COLOR_COFFEE_BROWN, 1));
        cbTables.addActionListener(e -> {
            TableItem selectedTable = (TableItem) cbTables.getSelectedItem();
            if (selectedTable != null) {
                cartSubject.setCurrentTable(selectedTable.getId());
                System.out.println(selectedTable.getId());
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlTopMain.add(cbTables, gbc);

        // 1.3 Nút XÓA SẠCH góc phải
        btnClearAll = new JButton("XÓA SẠCH");
        btnClearAll.setBackground(new Color(217, 83, 79)); // Đỏ mềm cảnh báo tương thích với nút Xóa bên Menu
        btnClearAll.setForeground(Color.WHITE);
        btnClearAll.setFont(new Font("Arial", Font.BOLD, 11));
        btnClearAll.setFocusPainted(false);
        btnClearAll.setPreferredSize(new Dimension(95, 30));
        btnClearAll.setBorder(null);
        btnClearAll.addActionListener(e -> confirmAndClearCart());

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 10, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        pnlTopMain.add(btnClearAll, gbc);

        add(pnlTopMain, BorderLayout.NORTH);

        // --- 2. Ở GIỮA: DANH SÁCH MÓN ĐÃ ĐẶT TÙY BIẾN (SỬA LỖI TRÙNG MÀU HEADER CHỮ
        // TRẮNG NỀN NÂU) ---
        String[] columns = { "Tên món / Diễn giải", "SL", "Tổng", "Thao tác" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 3;
            }
        };

        cartTable = new JTable(tableModel);
        cartTable.setRowHeight(55);
        cartTable.setBackground(Color.WHITE); // Giữ nguyên nền trắng cho danh sách hiển thị món ăn theo yêu cầu
        cartTable.setSelectionBackground(new Color(0xFF, 0xF2, 0xCC)); // Màu vàng kem dịu khi click chọn dòng
        cartTable.setSelectionForeground(COLOR_ESPRESSO_TEXT);
        cartTable.setGridColor(new Color(240, 240, 240));

        // =========================================================================
        // CẤU HÌNH TABLE HEADER ĐỂ KHÔNG BỊ KHUẤT CHỮ TRÊN WIN 11 / LOOK AND FEEL
        // =========================================================================
        JTableHeader tableHeader = cartTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(COLOR_COFFEE_BROWN); // Ép hình nền dải tiêu đề thành màu Nâu Đậm
        tableHeader.setForeground(Color.WHITE); // Ép chữ tiêu đề hiển thị màu Trắng tương phản cao
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 32)); // Tăng độ dày dặn thanh tiêu đề
        tableHeader.setReorderingAllowed(false);

        // Tạo Custom Cell Renderer cho Header để chống bị Look & Feel mặc định của
        // Windows ghi đè màu sắc
        tableHeader.setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JLabel headerLabel = new JLabel(value.toString(), SwingConstants.CENTER);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 12));
            headerLabel.setBackground(COLOR_COFFEE_BROWN);
            headerLabel.setForeground(Color.WHITE);
            headerLabel.setOpaque(true);
            headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, COLOR_LATTE_LIGHT)); // Đường kẻ mảnh phân
                                                                                                   // cột
            return headerLabel;
        });

        cartTable.getColumnModel().getColumn(0).setPreferredWidth(160);
        cartTable.getColumnModel().getColumn(0).setCellRenderer(new OrderDescriptionRenderer());

        TableActionCellEditorRenderer qtyController = new TableActionCellEditorRenderer(cartSubject, false);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(95);
        cartTable.getColumnModel().getColumn(1).setCellRenderer(qtyController);
        cartTable.getColumnModel().getColumn(1).setCellEditor(qtyController);

        cartTable.getColumnModel().getColumn(2).setPreferredWidth(75);

        TableActionCellEditorRenderer deleteController = new TableActionCellEditorRenderer(cartSubject, true);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        cartTable.getColumnModel().getColumn(3).setCellRenderer(deleteController);
        cartTable.getColumnModel().getColumn(3).setCellEditor(deleteController);

        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_COFFEE_BROWN, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. PHÍA DƯỚI: KHU VỰC TÍNH TIỀN & ĐIỀU KHIỂN NÂNG CAO ---
        JPanel pnlBottom = new JPanel();
        pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
        pnlBottom.setBackground(COLOR_WARM_CREAM); // Đồng bộ nền phần tính tiền
        pnlBottom.setBorder(BorderFactory.createEmptyBorder(12, 5, 5, 5));

        lblSubTotal = new JLabel("Tiền nước gốc: 0 đ");
        lblSubTotal.setFont(new Font("Arial", Font.BOLD, 12));
        lblSubTotal.setForeground(COLOR_LATTE_LIGHT); // Nhãn phụ dùng màu Nâu Latte nhẹ dịu

        lblTax = new JLabel("Thuế VAT (8%): 0 đ");
        lblTax.setFont(new Font("Arial", Font.BOLD, 12));
        lblTax.setForeground(COLOR_LATTE_LIGHT);

        lblTotal = new JLabel("TỔNG CỘNG: 0 đ");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18)); // Chữ to rõ ràng hơn
        lblTotal.setForeground(COLOR_ESPRESSO_TEXT); // Đổi từ đỏ sang Espresso đậm quý phái tương phản tốt

        lblSubTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTax.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTotal.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlBottom.add(lblSubTotal);
        pnlBottom.add(Box.createVerticalStrut(6));
        pnlBottom.add(lblTax);
        pnlBottom.add(Box.createVerticalStrut(8));

        JSeparator separator = new JSeparator();
        separator.setForeground(COLOR_LATTE_LIGHT);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlBottom.add(separator);
        pnlBottom.add(Box.createVerticalStrut(8));

        pnlBottom.add(lblTotal);
        pnlBottom.add(Box.createVerticalStrut(12));

        // Thanh công cụ bổ trợ (Hoàn tác, làm lại, sao chép đơn)
        JPanel pnlActionTools = new JPanel(new GridLayout(1, 3, 6, 0));
        pnlActionTools.setBackground(COLOR_WARM_CREAM);
        pnlActionTools.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlActionTools.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        btnUndo = new JButton("HOÀN TÁC");
        btnRedo = new JButton("LÀM LẠI");
        btnCopyOrder = new JButton("SAO CHÉP ĐƠN »");

        Font toolFont = new Font("Arial", Font.BOLD, 11);
        for (JButton btn : new JButton[] { btnUndo, btnRedo, btnCopyOrder }) {
            btn.setFont(toolFont);
            btn.setBackground(Color.WHITE);
            btn.setForeground(COLOR_COFFEE_BROWN);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(COLOR_LATTE_LIGHT, 1));
        }

        btnUndo.addActionListener(e -> cartSubject.undo());
        btnRedo.addActionListener(e -> cartSubject.redo());
        btnCopyOrder.addActionListener(e -> executeCopyOrderLogic());

        pnlActionTools.add(btnUndo);
        pnlActionTools.add(btnRedo);
        pnlActionTools.add(btnCopyOrder);
        pnlBottom.add(pnlActionTools);

        pnlBottom.add(Box.createVerticalStrut(12));

        // Nút Thanh Toán siêu nổi bật chiếm trọn hàng ngang đáy
        btnCheckout = new JButton("THANH TOÁN (IN HÓA ĐƠN)");
        btnCheckout.setBackground(COLOR_WARM_COPPER); // Chuyển sang màu Đồng ấm sáng ấn tượng mạnh
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setFont(new Font("Arial", Font.BOLD, 14));
        btnCheckout.setFocusPainted(false);
        btnCheckout.setBorder(null);
        btnCheckout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42)); // Tăng độ dày nút bấm cho thao tác mượt mà
        btnCheckout.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnCheckout.addActionListener(e -> {
            // 1. Kiểm tra giỏ hàng
            if (cartSubject.getCartItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống, không thể thanh toán!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Tạo đối tượng Invoice (Đảm bảo đã tính toán sơ bộ)
            Invoice invoice = createInvoiceFromCart();

            // 3. Mở Dialog thanh toán (Modal = true giúp dừng luồng code tại đây cho đến
            // khi đóng)
            PaymentService paymentService = new PaymentService();

            paymentService.prepareInvoice(
                    invoice,
                    0);
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            PaymentDialog paymentDialog = new PaymentDialog(parent, invoice);
            paymentDialog.setVisible(true);

            // 4. Xử lý sau khi PaymentDialog đóng
            // PaymentDialog chỉ trả về true nếu in hóa đơn thành công
            if (paymentDialog.isPaymentSuccessful()) {
                cartSubject.clearCurrentCart(); // Chỉ xóa khi đã chắc chắn in xong

                Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof MainFrame mainFrame) {
                    mainFrame.getInvoiceHistoryPanel().refresh();
                }

                JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Có thể ghi log hoặc bỏ qua nếu người dùng hủy thanh toán
                System.out.println("Thanh toán bị hủy hoặc lỗi.");
            }
        });

        pnlBottom.add(btnCheckout);

        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void confirmAndClearCart() {
        int response = JOptionPane.showConfirmDialog(this,
                "Xóa toàn bộ các món nước trong bàn này?",
                "Xác nhận xóa sạch",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.OK_OPTION) {
            cartSubject.clearCurrentCart();
        }
    }

    private void executeCopyOrderLogic() {
        TableItem currentTable = (TableItem) cbTables.getSelectedItem();
        if (currentTable == null)
            return;

        JPopupMenu tableMenu = new JPopupMenu("Chọn bàn sao chép đơn sang:");

        for (TableItem table : listTablesMemory) {
            if (StatusUtil.isTableFree(table.getStatus()) && table.getId() != currentTable.getId()) {
                JMenuItem item = new JMenuItem("Sao chép sang: " + table.getName());
                item.addActionListener(e -> {
                    // Ứng dụng phương thức deepCopy
                    boolean success = cartSubject.copyOrderToTable(table.getId());
                    if (success) {
                        table.setStatus(StatusUtil.TABLE_BUSY);
                        cbTables.repaint();
                        JOptionPane.showMessageDialog(this, "Đã sao chép đơn thành công sang " + table.getName());
                    }
                });
                tableMenu.add(item);
            }
        }

        if (tableMenu.getComponentCount() == 0) {
            JOptionPane.showMessageDialog(this, "Hiện tại không có bàn trống nào khả dụng để sao chép đơn!");
            return;
        }

        tableMenu.show(btnCopyOrder, 0, -tableMenu.getPreferredSize().height);
    }

    // =========================================================================
    // KHU VỰC SỬA LỖI LẶP CHỮ: Loại bỏ text trạng thái ghi tay (Trống)/(Đang ngồi)
    // =========================================================================
    private void loadTablesFromDatabase() {
        cbTables.removeAllItems();
        listTablesMemory.clear();

        // Cái này phải load ra từ db chứ!!!!!!!!!!!!!!!!!
        listTablesMemory.add(new TableItem(1, "Bàn số 01", StatusUtil.TABLE_FREE));
        listTablesMemory.add(new TableItem(2, "Bàn số 02", StatusUtil.TABLE_FREE));
        listTablesMemory.add(new TableItem(3, "Bàn số 03", StatusUtil.TABLE_FREE));
        listTablesMemory.add(new TableItem(4, "Bàn số 04", StatusUtil.TABLE_FREE));

        for (TableItem table : listTablesMemory) {
            cbTables.addItem(table);
        }

        if (cbTables.getItemCount() > 0) {
            cbTables.setSelectedIndex(0);
            TableItem firstTable = cbTables.getItemAt(0);
            if (firstTable != null) {
                cartSubject.setCurrentTable(firstTable.getId());
            }
        }
    }

    @Override
    public void update(Subject subject) {

        if (!(subject instanceof CartSubject)) {
            return;
        }

        CartSubject cart = (CartSubject) subject;

        cart.refreshTableStatus(
                listTablesMemory);

        cbTables.repaint();

        tableModel.setRowCount(0);

        double subtotal = 0;

        for (CartSubject.OrderItem order : cart.getCartItems()) {

            double amount = order.getItem().getFinalPrice()
                    * order.getQuantity();

            tableModel.addRow(new Object[] {
                    order.getItem().getName(),
                    order,
                    MoneyUtil.format(amount),
                    order
            });

            subtotal += amount;
        }

        // Lấy VAT rate từ AppConfig
        double vatRate = AppConfig.getInstance().getVatRate();
        double vat = subtotal * vatRate;
        double total = subtotal + vat;

        lblSubTotal.setText(
                "Tiền nước gốc: "
                        + MoneyUtil.format(subtotal));

        lblTax.setText("Thuế VAT (" + (vatRate * 100) + "%): " + MoneyUtil.format(vat));

        lblTotal.setText("TỔNG CỘNG: " + MoneyUtil.format(total));

    }

    private Invoice createInvoiceFromCart() {
        Invoice invoice = new Invoice(InvoiceIdGenerator.generate());
        List<InvoiceItem> items = new ArrayList<>();
        double subtotal = 0;

        for (CartSubject.OrderItem order : cartSubject.getCartItems()) {
            double price = order.getItem().getFinalPrice();
            int qty = order.getQuantity();
            double totalAmount = price * qty; // Tính tổng tiền

            // GIẢ SỬ: order.getItem().getId() lấy ID của món ăn
            // Cần truyền đủ 6 tham số theo đúng thứ tự mà class InvoiceItem yêu cầu
            items.add(new InvoiceItem(
                    order.getItem().getId(), // int id
                    order.getItem().getName(), // String name
                    qty, // int quantity
                    price, // double price
                    totalAmount, // double total amount
                    "" // String note (để rỗng nếu không có)
            ));

            subtotal += totalAmount;
        }

        invoice.setItems(items);
        invoice.setSubtotal(subtotal);
        invoice.setUserId(CurrentUser.getInstance().getUser().getUserId());
        invoice.setTableId(cartSubject.getCurrentTableId());
        invoice.setCreatedAt(DateTimeUtil.now());
        return invoice;
    }
}