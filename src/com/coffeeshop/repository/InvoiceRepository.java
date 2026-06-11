package com.coffeeshop.repository;

import com.coffeeshop.config.DatabaseConnection;
import com.coffeeshop.model.Invoice;
import com.coffeeshop.model.InvoiceItem;
import com.coffeeshop.model.PaymentMethod;

import java.sql.*;
import java.time.LocalDateTime;

public class InvoiceRepository {

    public boolean save(Invoice invoice) {
        String insertInvoice = """
                INSERT INTO hoaDon (
                    idHoaDon, idBan, idNguoiDung, tongTienChuaThue,
                    tienThueGiaTriGiaTang, tienGiamGiaKhuyenMai, tongTienThucThu,
                    phuongThucThanhToan, soTienKhachDua, tienTraLai, thoiGianTao
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String insertInvoiceItem = """
                INSERT INTO chiTietDonHang (
                    idHoaDon, idMonAn, soLuongDat, giaBanTaiThoiDiem, tongTienSauTrangTri
                ) VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(insertInvoice);
                    PreparedStatement psDetail = conn.prepareStatement(insertInvoiceItem)) {

                ps.setString(1, invoice.getInvoiceId());
                if (invoice.getTableId() == null)
                    ps.setNull(2, Types.INTEGER);
                else
                    ps.setInt(2, invoice.getTableId());
                if (invoice.getUserId() == null)
                    ps.setNull(3, Types.INTEGER);
                else
                    ps.setInt(3, invoice.getUserId());

                ps.setDouble(4, invoice.getSubtotal());
                ps.setDouble(5, invoice.getVatAmount());
                ps.setDouble(6, invoice.getDiscountAmount());
                ps.setDouble(7, invoice.getTotalAmount());
                ps.setString(8, invoice.getPaymentMethod().name());
                ps.setDouble(9, invoice.getAmountPaid());
                ps.setDouble(10, invoice.getChangeAmount());
                ps.setTimestamp(11, Timestamp
                        .valueOf(invoice.getCreatedAt() != null ? invoice.getCreatedAt() : LocalDateTime.now()));

                ps.executeUpdate();

                for (InvoiceItem item : invoice.getItems()) {
                    psDetail.setString(1, invoice.getInvoiceId());
                    psDetail.setInt(2, item.getDishId());
                    psDetail.setInt(3, item.getQuantityOrdered());
                    psDetail.setDouble(4, item.getPriceAtTime());
                    psDetail.setDouble(5, item.getTotalAmountAfterDiscount());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();

                conn.commit();
                return true;
            } catch (Exception ex) {
                conn.rollback();
                ex.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Invoice findById(String invoiceId) {
        String sql = "SELECT * FROM hoaDon WHERE idHoaDon = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, invoiceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Invoice invoice = new Invoice(rs.getString("idHoaDon"));
                invoice.setTableId((Integer) rs.getObject("idBan"));
                invoice.setUserId((Integer) rs.getObject("idNguoiDung"));
                invoice.setSubtotal(rs.getDouble("tongTienChuaThue"));
                invoice.setVatAmount(rs.getDouble("tienThueGiaTriGiaTang"));
                invoice.setDiscountAmount(rs.getDouble("tienGiamGiaKhuyenMai"));
                invoice.setTotalAmount(rs.getDouble("tongTienThucThu"));
                invoice.setPaymentMethod(PaymentMethod.valueOf(rs.getString("phuongThucThanhToan")));
                invoice.setAmountPaid(rs.getDouble("soTienKhachDua"));
                invoice.setChangeAmount(rs.getDouble("tienTraLai"));
                invoice.setCreatedAt(rs.getTimestamp("thoiGianTao").toLocalDateTime());

                // Load invoice items
                invoice.setItems(loadInvoiceItems(conn, invoiceId));

                return invoice;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private java.util.List<InvoiceItem> loadInvoiceItems(java.sql.Connection conn, String invoiceId) {
        java.util.List<InvoiceItem> items = new java.util.ArrayList<>();
        String sql = "SELECT * FROM chiTietDonHang WHERE idHoaDon = ?";
        try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, invoiceId);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                InvoiceItem item = new InvoiceItem(
                        rs.getInt("idMonAn"),
                        "", // dishName will be empty (could load from monAn table if needed)
                        rs.getInt("soLuongDat"),
                        rs.getDouble("giaBanTaiThoiDiem"),
                        rs.getDouble("tongTienSauTrangTri"));
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}