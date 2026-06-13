package com.coffeeshop.model;

public class CoffeeItem implements MenuItem {
    private int id;
    private String name;
    private String category;
    private double basePrice;
    private String description;
    private String icon;
    private String status;

    public CoffeeItem(int id, String name, String category, double price, String description, String icon, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.basePrice = price;
        this.description = description;
        this.icon = icon;
        this.status = status;
    }

    // ✅ Copy constructor
    public CoffeeItem(CoffeeItem other) {
        this.id = other.getId();
        this.name = other.getName();
        this.category = other.getCategory();
        this.basePrice = other.getBasePrice();
        this.description = other.getDescription();
        this.icon = other.getIcon();
        this.status = other.getStatus();
    }

    @Override public int getId() { return id; }
    @Override public String getName() { return name; }
    @Override public String getCategory() { return category; }
    @Override public double getBasePrice() { return basePrice; }
    @Override public double getFinalPrice() { return basePrice; }
    @Override public String getDescription() { return description; }
    @Override public String getIcon() { return icon; }
    @Override public String getStatus() { return status; }

    @Override public void setName(String name) { this.name = name; }
    @Override public void setCategory(String category) { this.category = category; }
    @Override public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    @Override public void setDescription(String description) { this.description = description; }
    @Override public void setIcon(String icon) { this.icon = icon; }
    @Override public void setStatus(String status) { this.status = status; }

    // ✅ Clone với covariant return type
    @Override
    public CoffeeItem clone() {
        return new CoffeeItem(this);
    }
}
