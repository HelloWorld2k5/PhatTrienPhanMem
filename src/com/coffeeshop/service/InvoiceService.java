package com.coffeeshop.service;

import com.coffeeshop.model.Invoice;
import com.coffeeshop.repository.InvoiceRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class InvoiceService {
    private final InvoiceRepository invoiceRepository = new InvoiceRepository();
    private final PaymentService paymentService = new PaymentService();

    /**
     * Validate invoice before saving
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

    public String generateInvoiceId() {
        return "INV"
                + LocalDateTime.now().toString().replace("-", "").replace(":", "").replace(".", "").replace("T", "") +
                UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    public boolean createAndSaveInvoice(Invoice invoice, double discount, double cashReceived) {
        try {
            validateInvoice(invoice);

            if (discount < 0) {
                throw new IllegalArgumentException("Discount cannot be negative");
            }
            if (cashReceived <= 0) {
                throw new IllegalArgumentException("Cash received must be greater than 0");
            }

            paymentService.calculateInvoice(invoice, discount, cashReceived);
            if (invoice.getInvoiceId() == null || invoice.getInvoiceId().isBlank()) {
                invoice.setInvoiceId(generateInvoiceId());
            }
            invoice.setCreatedAt(LocalDateTime.now());
            return invoiceRepository.save(invoice);
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error creating invoice: " + e.getMessage());
            throw new RuntimeException("Failed to create invoice", e);
        }
    }

    /**
     * Create and save invoice with custom VAT rate (override from config)
     */
    public boolean createAndSaveInvoice(Invoice invoice, double vatRate, double discount, double cashReceived) {
        try {
            validateInvoice(invoice);

            if (vatRate < 0 || vatRate > 1) {
                throw new IllegalArgumentException("VAT rate must be between 0 and 1");
            }
            if (discount < 0) {
                throw new IllegalArgumentException("Discount cannot be negative");
            }
            if (cashReceived <= 0) {
                throw new IllegalArgumentException("Cash received must be greater than 0");
            }

            paymentService.calculateInvoice(invoice, vatRate, discount, cashReceived);
            if (invoice.getInvoiceId() == null || invoice.getInvoiceId().isBlank()) {
                invoice.setInvoiceId(generateInvoiceId());
            }
            invoice.setCreatedAt(LocalDateTime.now());
            return invoiceRepository.save(invoice);
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error creating invoice: " + e.getMessage());
            throw new RuntimeException("Failed to create invoice", e);
        }
    }

    public Invoice duplicateInvoice(String invoiceId) {
        try {
            if (invoiceId == null || invoiceId.isBlank()) {
                throw new IllegalArgumentException("Invoice ID cannot be null or empty");
            }

            Invoice original = invoiceRepository.findById(invoiceId);
            if (original == null) {
                throw new IllegalArgumentException("Invoice not found: " + invoiceId);
            }

            // Prototype Pattern: sao chép hóa đơn
            return original.deepCopy();
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error duplicating invoice: " + e.getMessage());
            throw new RuntimeException("Failed to duplicate invoice", e);
        }
    }
}