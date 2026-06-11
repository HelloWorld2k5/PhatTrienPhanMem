package com.coffeeshop.repository;

import com.coffeeshop.config.DatabaseConnection;
import com.coffeeshop.model.User;
import com.coffeeshop.model.UserRole;
import com.coffeeshop.model.UserStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public User findByUsername(String username) {
        String sql = "SELECT * FROM nguoiDung WHERE tenDangNhap = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findById(int id) {
        String sql = "SELECT * FROM nguoiDung WHERE idNguoiDung = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        String sql = "SELECT * FROM nguoiDung ORDER BY idNguoiDung DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean insert(User user) {
        String sql = """
                INSERT INTO nguoiDung (tenDangNhap, matKhauMaHoa, hoTenNguoiDung, vaiTroNguoiDung, trangThaiHoatDong)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEncryptedPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getUserRole().name());
            ps.setString(5, user.getUserStatus().name());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(User user) {
        String sql = """
                UPDATE nguoiDung
                SET tenDangNhap = ?, matKhauMaHoa = ?, hoTenNguoiDung = ?, vaiTroNguoiDung = ?, trangThaiHoatDong = ?
                WHERE idNguoiDung = ?
                """;
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEncryptedPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getUserRole().name());
            ps.setString(5, user.getUserStatus().name());
            ps.setInt(6, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setStatus(int userId, UserStatus status) {
        String sql = "UPDATE nguoiDung SET trangThaiHoatDong = ? WHERE idNguoiDung = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("idNguoiDung"),
            rs.getString("tenDangNhap"),
            rs.getString("matKhauMaHoa"),
            rs.getString("hoTenNguoiDung"),
            UserRole.valueOf(rs.getString("vaiTroNguoiDung")),
            UserStatus.valueOf(rs.getString("trangThaiHoatDong"))
        );
    }
}