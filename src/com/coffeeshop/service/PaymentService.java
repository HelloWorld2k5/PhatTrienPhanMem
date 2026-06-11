package com.coffeeshop.service;

import com.coffeeshop.config.AppConfig;
import com.coffeeshop.model.Invoice;

public class PaymentService {

    /**
     * Validate invoice data before calculation
     */
    private void validateInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
            throw new IllegalArgumentException("Invoice must have at least one item");
        }
        if (invoice.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Payment method is required");
        }
    }

    /**
     * Validate payment amounts
     */
    private void validatePaymentAmounts(double subtotal, double discount, double cashReceived) {
        if (subtotal < 0) {
            throw new IllegalArgumentException("Subtotal cannot be negative");
        }
        if (discount < 0 || discount > subtotal) {
            throw new IllegalArgumentException("Discount must be between 0 and subtotal");
        }
        if (cashReceived <= 0) {
            throw new IllegalArgumentException("Cash received must be greater than 0");
        }
    }

    public void calculateInvoice(Invoice invoice, double discount, double cashReceived) {
        validateInvoice(invoice);

        double vatRate = AppConfig.getInstance().getVatRate();

        invoice.setDiscountAmount(discount);
        invoice.setAmountPaid(cashReceived);

        double subtotal = invoice.getItems()
                .stream()
                .mapToDouble(item -> item.getTotalAmountAfterDiscount())
                .sum();

        validatePaymentAmounts(subtotal, discount, cashReceived);

        invoice.setSubtotal(subtotal);
        invoice.setVatAmount(subtotal * vatRate);
        double total = subtotal + invoice.getVatAmount() - discount;
        invoice.setTotalAmount(total);
        invoice.setChangeAmount(cashReceived - total);
    }

    /**
     * Calculate invoice with custom VAT rate (override from config)
     */
    public void calculateInvoice(Invoice invoice, double vatRate, double discount, double cashReceived) {
        validateInvoice(invoice);

        if (vatRate < 0 || vatRate > 1) {
            throw new IllegalArgumentException("VAT rate must be between 0 and 1");
        }

        invoice.setDiscountAmount(discount);
        invoice.setAmountPaid(cashReceived);

        double subtotal = invoice.getItems()
                .stream()
                .mapToDouble(item -> item.getTotalAmountAfterDiscount())
                .sum();

        validatePaymentAmounts(subtotal, discount, cashReceived);

        invoice.setSubtotal(subtotal);
        invoice.setVatAmount(subtotal * vatRate);
        double total = subtotal + invoice.getVatAmount() - discount;
        invoice.setTotalAmount(total);
        invoice.setChangeAmount(cashReceived - total);
    }
}