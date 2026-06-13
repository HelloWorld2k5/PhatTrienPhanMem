package com.coffeeshop.repository;

import com.coffeeshop.config.DatabaseConnection;
import com.coffeeshop.factory.AbstractMenuItemFactory;
import com.coffeeshop.factory.MenuItemFactoryRegistry;
import com.coffeeshop.model.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuRepository {

    public List<MenuItem> getAllMenuItems() {
    List<MenuItem> menu = new ArrayList<>();
    String sql = "SELECT idMonAn, tenMonAn, danhMucMonAn, giaMonAnCoBan, moTaMonAn, bieuTuongMonAn, trangThaiMonAn FROM monAn";

    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            try {
                String category = rs.getString("danhMucMonAn");
                // Lấy Factory tương ứng từ Registry
                AbstractMenuItemFactory factory = MenuItemFactoryRegistry.getFactory(category);
                
                if (factory != null) {
                    MenuItem item = factory.createMenuItem(
                        rs.getInt("idMonAn"),
                        rs.getString("tenMonAn"),
                        category,
                        rs.getDouble("giaMonAnCoBan"),
                        rs.getString("moTaMonAn"),
                        rs.getString("bieuTuongMonAn"),
                        rs.getString("trangThaiMonAn")
                    );
                    menu.add(item);
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi khởi tạo item ID: " + rs.getInt("idMonAn"));
            }
        }
    } catch (SQLException e) {
        System.err.println("Lỗi kết nối DB: " + e.getMessage());
    }
    return menu;
}

    /**
     * Thêm mới một món ăn vào bảng monAn trong Database
     * @param item Đối tượng món ăn chứa dữ liệu cần lưu
     * @return true nếu thêm thành công, false nếu thất bại (trùng tên hoặc lỗi SQL)
     */
    public boolean insertNewMenuItem(MenuItem item) {
        String sql = "INSERT INTO monAn (tenMonAn, danhMucMonAn, giaMonAnCoBan, moTaMonAn, bieuTuongMonAn, trangThaiMonAn) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        // Sử dụng cấu trúc kết nối chuẩn DatabaseConnection.getInstance().getConnection() của hệ thống bạn
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getCategory());     // Nhận các chuỗi ENUM: 'COFFEE_MACHINE', 'BAKERY',...
            pstmt.setDouble(3, item.getBasePrice());
            pstmt.setString(4, item.getDescription());
            pstmt.setString(5, item.getIcon());         // Mặc định là '☕' hoặc chuỗi do người dùng nhập
            pstmt.setString(6, item.getStatus());       // Mặc định truyền vào là 'AVAILABLE'

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu ghi nhận 1 dòng dữ liệu được chèn thành công

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm món ăn mới vào database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

public boolean deleteMenuItem(int id) {
        // Cập nhật câu lệnh SQL cho khớp với bảng monAn
        String sql = "DELETE FROM monAn WHERE idMonAn = ?"; 
        
        // Sử dụng DatabaseConnection.getInstance().getConnection() thay vì biến 'connection' chưa khai báo
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa món ăn: " + e.getMessage());
            return false;
        }
    }
    
public boolean updateMenuItem(MenuItem item) {
    // Câu lệnh SQL update đầy đủ các trường
    String sql = "UPDATE monAn SET " +
                 "tenMonAn = ?, " +
                 "danhMucMonAn = ?, " +
                 "giaMonAnCoBan = ?, " +
                 "moTaMonAn = ?, " +
                 "bieuTuongMonAn = ?, " +
                 "trangThaiMonAn = ? " +
                 "WHERE idMonAn = ?";
    
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        // Map các thuộc tính từ đối tượng MenuItem vào câu lệnh SQL
        pstmt.setString(1, item.getName());           // tenMonAn
        pstmt.setString(2, item.getCategory());       // danhMucMonAn
        pstmt.setDouble(3, item.getBasePrice());      // giaMonAnCoBan
        pstmt.setString(4, item.getDescription());    // moTaMonAn
        pstmt.setString(5, item.getIcon());           // bieuTuongMonAn
        pstmt.setString(6, item.getStatus());         // trangThaiMonAn
        pstmt.setInt(7, item.getId());                // idMonAn (Điều kiện WHERE)
        
        // Thực thi update
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
        
    } catch (SQLException e) {
        System.err.println("Lỗi khi cập nhật thông tin món ăn: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
}