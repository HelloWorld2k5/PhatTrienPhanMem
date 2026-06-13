package com.coffeeshop.view;

import com.coffeeshop.model.Invoice;
import com.coffeeshop.repository.InvoiceRepository;
import com.coffeeshop.util.MoneyUtil;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InvoiceHistoryPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private final InvoiceRepository invoiceRepository = new InvoiceRepository();

    // Màu cam đất (Earthy Orange)
    private final Color EARTHY_ORANGE = new Color(202, 81, 0);

    public InvoiceHistoryPanel() {
        // ... (Giữ nguyên phần khởi tạo như cũ)
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 246, 247));

        // ... (Header và Table setup)
        String[] columns = { "Mã Hóa Đơn", "Bàn", "Thành Tiền", "Thời Gian", "Hình thức", "Hành động" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField()));

        add(new JScrollPane(table), BorderLayout.CENTER);
        loadDataToTable();
    }

    public void loadDataToTable() {
        tableModel.setRowCount(0);
        List<Invoice> invoices = invoiceRepository.getAllInvoices();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Invoice inv : invoices) {
            Object[] row = {
                    inv.getInvoiceId(),
                    inv.getTableId() != null ? "Bàn " + inv.getTableId() : "Mang về",
                    MoneyUtil.format(inv.getTotalAmount()),
                    inv.getCreatedAt() != null ? inv.getCreatedAt().format(formatter) : "",
                    inv.getPaymentMethod(),
                    "Xem Chi Tiết"
            };
            tableModel.addRow(row);
        }
    }

    public void refresh() {
        loadDataToTable();
    }

    // --- CÁC INNER CLASSES PHẢI ĐẶT TRONG DẤU NGOẶC CỦA InvoiceHistoryPanel ---

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(EARTHY_ORANGE); // Màu cam đất
            setForeground(Color.WHITE); // Chữ trắng
            setBorderPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;

        public ButtonEditor(JTextField txt) {
            super(txt);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(EARTHY_ORANGE); // Màu cam đất
            button.setForeground(Color.WHITE); // Chữ trắng
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                fireEditingStopped();
                int row = table.getSelectedRow();
                if (row != -1) {
                    String invoiceId = table.getValueAt(row, 0).toString();
                    Invoice fullInvoice = invoiceRepository.findById(invoiceId);

                    if (fullInvoice != null) {
                        InvoiceDetailDialog dialog = new InvoiceDetailDialog(
                                (JFrame) SwingUtilities.getWindowAncestor(table), fullInvoice.deepCopy());
                        dialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy chi tiết hóa đơn!");
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            label = "Xem Chi Tiết";
            button.setText(label);
            return button;
        }

        public Object getCellEditorValue() {
            return label;
        }
    }
} // <--- Đảm bảo class InvoiceHistoryPanel KẾT THÚC Ở ĐÂY (dấu ngoặc này đóng tất
  // cả)