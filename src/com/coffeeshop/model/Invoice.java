package com.coffeeshop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Hoá đơn
public class Invoice {
    private String invoiceId;
    private Integer tableId;
    private Integer userId;
    private List<InvoiceItem> items = new ArrayList<>();

    private double subtotal;
    private double vatAmount;
    private double discountAmount;
    private double totalAmount;
    private PaymentMethod paymentMethod;
    private double amountPaid;
    private double changeAmount;
    private LocalDateTime createdAt;

    public Invoice() {
    }

    public Invoice(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Invoice deepCopy() {
        Invoice copy = new Invoice(this.invoiceId);
        copy.tableId = this.tableId;
        copy.userId = this.userId;
        copy.subtotal = this.subtotal;
        copy.vatAmount = this.vatAmount;
        copy.discountAmount = this.discountAmount;
        copy.totalAmount = this.totalAmount;
        copy.paymentMethod = this.paymentMethod;
        copy.amountPaid = this.amountPaid;
        copy.changeAmount = this.changeAmount;
        copy.createdAt = this.createdAt;

        // Deep copy items
        copy.items = new ArrayList<>();
        for (InvoiceItem item : this.items) {
            copy.items.add(item.clone());
        }

        return copy;
    }

    public void recalculate(double vatRate) {
        subtotal = items.stream().mapToDouble(InvoiceItem::getTotalAmountAfterDiscount).sum();
        vatAmount = subtotal * vatRate;
        totalAmount = subtotal + vatAmount - discountAmount;
        changeAmount = amountPaid - totalAmount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}