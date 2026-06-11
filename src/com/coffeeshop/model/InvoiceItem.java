package com.coffeeshop.model;

/*
 * Prototype Pattern:
 * Mỗi dòng món trong hóa đơn có thể tạo bản sao của chính nó
 * để hỗ trợ việc deep copy khi sao chép hóa đơn.
 */
// Chi tiết hoá đơn
public class InvoiceItem {
    private int dishId;
    private String dishName;
    private int quantityOrdered;
    private double priceAtTime;
    private double totalAmountAfterDiscount;

    public InvoiceItem() {}

    public InvoiceItem(int dishId, String dishName, int quantityOrdered,
                       double priceAtTime, double totalAmountAfterDiscount) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.quantityOrdered = quantityOrdered;
        this.priceAtTime = priceAtTime;
        this.totalAmountAfterDiscount = totalAmountAfterDiscount;
    }

    public InvoiceItem(InvoiceItem other) {
        dishId = other.getDishId();
        dishName = other.getDishName();
        quantityOrdered = other.getQuantityOrdered();
        priceAtTime = other.getPriceAtTime();
        totalAmountAfterDiscount = other.getTotalAmountAfterDiscount();
    }

    public InvoiceItem clone() {
        return new InvoiceItem(this);
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(int quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public double getPriceAtTime() {
        return priceAtTime;
    }

    public void setPriceAtTime(double priceAtTime) {
        this.priceAtTime = priceAtTime;
    }

    public double getTotalAmountAfterDiscount() {
        return totalAmountAfterDiscount;
    }

    public void setTotalAmountAfterDiscount(double totalAmountAfterDiscount) {
        this.totalAmountAfterDiscount = totalAmountAfterDiscount;
    }
}