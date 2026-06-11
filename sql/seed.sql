-- =========================
-- DU LIEU MẪU
-- =========================

-- 1) CAU HINH HE THONG
INSERT INTO cauHinhHeThong (khoaCauHinh, giaTriCauHinh) VALUES
('TEN_QUAN', 'Coffee Shop'),
('VAT_RATE', '0.08'),
('CURRENCY', 'VND'),
('HOTLINE', '0123456789')
ON DUPLICATE KEY UPDATE giaTriCauHinh = VALUES(giaTriCauHinh);

-- 2) NGUOI DUNG
-- Mật khẩu admin là admin123
INSERT INTO nguoiDung (idNguoiDung, tenDangNhap, matKhauMaHoa, hoTenNguoiDung, vaiTroNguoiDung, trangThaiHoatDong) VALUES
(1, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Nguyen The Truong Admin', 'ADMIN', 'ACTIVE'),

-- 3) BAN
INSERT INTO banQuanAn (idBan, tenBan, trangThaiBan) VALUES
(1, 'B1', 'FREE'),
(2, 'B2', 'BUSY'),
(3, 'B3', 'FREE'),
(4, 'B4', 'FREE');

-- 4) MON AN
INSERT INTO monAn (idMonAn, tenMonAn, danhMucMonAn, giaMonAnCoBan, moTaMonAn, bieuTuongMonAn, trangThaiMonAn) VALUES
(1, 'Espresso', 'COFFEE_MACHINE', 35000.00, 'Ca phe den dam vi', '☕', 'AVAILABLE'),
(2, 'Latte', 'COFFEE_MACHINE', 45000.00, 'Ca phe sua beo mem', '☕', 'AVAILABLE'),
(3, 'Ca phe sua da', 'COFFEE_TRADITIONAL', 30000.00, 'Ca phe phin truyen thong', '🧋', 'AVAILABLE'),
(4, 'Matcha Latte', 'COFFEE_MACHINE', 48000.00, 'Tra xanh sua beo', '🍵', 'AVAILABLE'),
(5, 'Banh croissant', 'BAKERY', 25000.00, 'Banh bo xop', '🥐', 'AVAILABLE'),
(6, 'Banh pho mai', 'BAKERY', 28000.00, 'Banh ngot kem pho mai', '🍰', 'AVAILABLE'),
(7, 'Combo Sang', 'COMBO', 85000.00, '1 latte + 1 croissant', '🎁', 'AVAILABLE');

-- 5) THANH PHAN COMBO
INSERT INTO thanhPhanCombo (idCombo, idMonAnCon, soLuongMonCon) VALUES
(7, 2, 1),
(7, 5, 1);

-- 6) TUY CHON MON
INSERT INTO tuyChonMon (idTuyChon, tenTuyChon, giaPhuThu, trangThaiTuyChon) VALUES
(1, 'Extra Shot', 10000.00, 'AVAILABLE'),
(2, 'Sua Hanh Nhan', 12000.00, 'AVAILABLE'),
(3, 'Caramel Syrup', 8000.00, 'AVAILABLE'),
(4, 'Kem Cheese', 15000.00, 'AVAILABLE');

-- 7) HOA DON
INSERT INTO hoaDon (
    idHoaDon, idBan, idNguoiDung, trangThaiHoaDon,
    tongTienChuaThue, tienThueGiaTriGiaTang, tienGiamGiaKhuyenMai,
    tongTienThucThu, phuongThucThanhToan, soTienKhachDua, tienTraLai
) VALUES
('INV20260610001', 2, 1, 'PAID',
 130000.00, 10400.00, 5000.00,
 135400.00, 'CASH', 200000.00, 64600.00),

('INV20260610002', 1, 1, 'PAID',
 76000.00, 6080.00, 0.00,
 82080.00, 'TRANSFER', 82080.00, 0.00);

-- 8) CHI TIET DON HANG
INSERT INTO chiTietDonHang (
    idChiTietDonHang, idHoaDon, idMonAn, soLuongDat,
    giaBanTaiThoiDiem, tongTienSauTrangTri
) VALUES
(1, 'INV20260610001', 2, 1, 45000.00, 57000.00),
(2, 'INV20260610001', 5, 1, 25000.00, 25000.00),
(3, 'INV20260610001', 3, 2, 30000.00, 48000.00),

(4, 'INV20260610002', 7, 1, 85000.00, 85000.00);

-- 9) CHI TIET TUY CHON MON DAT
INSERT INTO chiTietTuyChonMonDat (idChiTietDonHang, idTuyChon, giaPhuThuTaiThoiDiem) VALUES
(1, 1, 10000.00),
(1, 3, 8000.00),
(3, 2, 12000.00),
(4, 4, 15000.00);