package com.coffeeshop.view;

import com.coffeeshop.adapter.ExcelInvoiceAdapter;
import com.coffeeshop.adapter.InvoicePrinter;
import com.coffeeshop.adapter.TxtInvoiceAdapter;
import com.coffeeshop.model.Invoice;
import com.coffeeshop.model.PaymentMethod;
import com.coffeeshop.service.PaymentService;
import com.coffeeshop.util.MoneyUtil;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PaymentDialog extends JDialog {
    private final JTextField txtCashIn = new JTextField(15);
    private final JTextField txtChange = new JTextField(15);

    private final JLabel lblSubtotal = new JLabel();
    private final JLabel lblVat = new JLabel();
    private final JLabel lblDiscount = new JLabel();
    private final JLabel lblGrandTotal = new JLabel();

    private final JComboBox<PaymentMethod> cboPaymentMethod = new JComboBox<>(PaymentMethod.values());

    private final JComboBox<String> cboPrintFormat = new JComboBox<>(new String[] { "TXT", "EXCEL" });

    private final PaymentService paymentService = new PaymentService();
    private final Invoice invoice;

    private boolean paymentSuccessful = false;

    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }

    public PaymentDialog(Frame parent, Invoice invoice) {
        super(parent, "Chi tiết thanh toán", true);
        this.invoice = invoice;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(760, 560);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(12, 12));

        add(buildInvoiceInfoPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        txtCashIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateChange();
            }
        });

        updateChange();
    }

    private JPanel buildInvoiceInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        panel.setBackground(Color.WHITE);

        JLabel lb1 = new JLabel("Tạm tính:");
        JLabel lb2 = new JLabel("VAT:");
        JLabel lb3 = new JLabel("Giảm giá:");
        JLabel lb4 = new JLabel("Tổng thanh toán:");

        Font boldFont = new Font("Arial", Font.BOLD, 13);
        Font totalFont = new Font("Arial", Font.BOLD, 15);

        lb1.setFont(boldFont);
        lb2.setFont(boldFont);
        lb3.setFont(boldFont);
        lb4.setFont(totalFont);

        lblSubtotal.setText(MoneyUtil.format(invoice.getSubtotal()));
        lblVat.setText(MoneyUtil.format(invoice.getVatAmount()));
        lblDiscount.setText(MoneyUtil.format(invoice.getDiscountAmount()));
        lblGrandTotal.setText(MoneyUtil.format(invoice.getTotalAmount()));

        lblGrandTotal.setFont(totalFont);

        panel.add(lb1);
        panel.add(lblSubtotal);
        panel.add(lb2);
        panel.add(lblVat);
        panel.add(lb3);
        panel.add(lblDiscount);
        panel.add(lb4);
        panel.add(lblGrandTotal);

        return panel;
    }

    private JPanel buildCenterPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setBackground(new Color(245, 246, 247));

        JPanel paymentPanel = new JPanel(new GridBagLayout());
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Thanh toán"));
        paymentPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCashIn.setColumns(14);
        txtChange.setColumns(14);
        txtChange.setEditable(false);
        txtChange.setFocusable(false);

        int y = 0;

        addFormRow(paymentPanel, gbc, y++, "Phương thức thanh toán:", cboPaymentMethod);
        addFormRow(paymentPanel, gbc, y++, "Tiền khách đưa:", txtCashIn);
        addFormRow(paymentPanel, gbc, y++, "Tiền trả lại:", txtChange);
        addFormRow(paymentPanel, gbc, y++, "Định dạng in:", cboPrintFormat);

        JPanel quickPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        quickPanel.setBorder(BorderFactory.createTitledBorder("Mệnh giá nhanh"));
        quickPanel.setBackground(Color.WHITE);

        JButton btn50k = new JButton("50.000 đ");
        btn50k.addActionListener(e -> txtCashIn.setText("50000"));

        JButton btn100k = new JButton("100.000 đ");
        btn100k.addActionListener(e -> txtCashIn.setText("100000"));

        JButton btn200k = new JButton("200.000 đ");
        btn200k.addActionListener(e -> txtCashIn.setText("200000"));

        JButton btn500k = new JButton("500.000 đ");
        btn500k.addActionListener(e -> txtCashIn.setText("500000"));

        for (JButton btn : new JButton[] { btn50k, btn100k, btn200k, btn500k }) {
            btn.setFocusPainted(false);
            quickPanel.add(btn);
        }

        wrapper.add(paymentPanel, BorderLayout.CENTER);
        wrapper.add(quickPanel, BorderLayout.SOUTH);

        return wrapper;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int y, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        panel.setBackground(new Color(245, 246, 247));

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> dispose());

        JButton btnPreview = new JButton("Xác nhận & In hóa đơn");
        btnPreview.setFocusPainted(false);
        btnPreview.setBackground(new Color(184, 115, 51));
        btnPreview.setForeground(Color.BLACK);
        btnPreview.addActionListener(e -> proceedToPreview());

        panel.add(btnCancel);
        panel.add(btnPreview);

        return panel;
    }

    private InvoicePrinter getSelectedPrinter() {
        String format = (String) cboPrintFormat.getSelectedItem();
        if ("EXCEL".equalsIgnoreCase(format)) {
            return new ExcelInvoiceAdapter();
        }
        return new TxtInvoiceAdapter();
    }

    private void updateChange() {
        try {
            String text = txtCashIn.getText().trim();
            if (text.isEmpty()) {
                txtChange.setText("");
                return;
            }

            double cashIn = Double.parseDouble(text);
            double total = invoice.getTotalAmount();
            double change = cashIn - total;

            if (change < 0) {
                txtChange.setText("Thiếu " + MoneyUtil.format(Math.abs(change)));
            } else {
                txtChange.setText(MoneyUtil.format(change));
            }
        } catch (NumberFormatException ex) {
            txtChange.setText("Không hợp lệ");
        }
    }

    private void proceedToPreview() {
        try {
            double cashIn = Double.parseDouble(txtCashIn.getText().trim());

            if (cashIn < invoice.getTotalAmount()) {
                JOptionPane.showMessageDialog(this, "Khách đưa chưa đủ tiền!");
                return;
            }

            PaymentMethod method = (PaymentMethod) cboPaymentMethod.getSelectedItem();
            if (method == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phương thức thanh toán!");
                return;
            }

            invoice.setPaymentMethod(method);
            invoice.setAmountPaid(cashIn);
            invoice.setChangeAmount(cashIn - invoice.getTotalAmount());

            boolean saved = paymentService.saveInvoice(invoice);
            if (!saved) {
                JOptionPane.showMessageDialog(this, "Không thể lưu hóa đơn!");
                return;
            }

            InvoicePrinter printer = getSelectedPrinter();
            String format = (String) cboPrintFormat.getSelectedItem();
            String extension = "EXCEL".equalsIgnoreCase(format) ? ".xlsx" : ".txt";
            String fileName = "HoaDon_" + invoice.getInvoiceId() + extension;

            Invoice clonedInvoice = invoice.deepCopy();
            printer.print(clonedInvoice, fileName);

            paymentSuccessful = true;

            JOptionPane.showMessageDialog(
                    this,
                    "Thanh toán thành công!\n"
                            + "Đã in file: " + fileName + "\n"
                            + "Tiền trả lại: " + MoneyUtil.format(invoice.getChangeAmount()));

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tiền khách đưa không hợp lệ!");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi in hóa đơn: " + ex.getMessage());
        }
    }
}