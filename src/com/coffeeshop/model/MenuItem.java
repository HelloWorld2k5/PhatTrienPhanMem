
package com.coffeeshop.model;

public interface MenuItem {

    int getId();                // idMonAn
    String getName();           // tenMonAn
    String getCategory();       // danhMucMonAn
    double getBasePrice();      // giaMonAnCoBan
    double getFinalPrice();     // Giá thực tế sau khi dùng Decorator thêm tuỳ chọn
    String getDescription();    // moTaMonAn
    String getIcon();           // bieuTuongMonAn (Icon)
    String getStatus();         // trangThaiMonAn ('AVAILABLE' hoặc 'OUT_OF_STOCK')

    void setName(String name);
    void setCategory(String category);
    void setBasePrice(double basePrice);
    void setDescription(String description);
    void setIcon(String icon);
    void setStatus(String status);
    
    MenuItem clone();
}

