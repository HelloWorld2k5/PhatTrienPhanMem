package com.coffeeshop.repository;

import com.coffeeshop.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SystemConfigRepository {

    public String getConfigValue(String configKey) {
        String sql = "SELECT giaTriCauHinh FROM cauHinhHeThong WHERE khoaCauHinh = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, configKey);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("giaTriCauHinh");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getConfigValueAsDouble(String configKey) {
        String value = getConfigValue(configKey);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0.0;
    }

    public void saveConfigValue(String configKey, String configValue) {
        String sql = """
                INSERT INTO cauHinhHeThong (khoaCauHinh, giaTriCauHinh) VALUES (?, ?)
                ON DUPLICATE KEY UPDATE giaTriCauHinh = VALUES(giaTriCauHinh)
                """;
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, configKey);
            ps.setString(2, configValue);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
