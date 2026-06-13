package com.coffeeshop.config;

import com.coffeeshop.repository.SystemConfigRepository;

public class AppConfig {
    private static AppConfig instance;

    // Thông tin ứng dụng mặc định
    private String appName;
    private String appVersion;
    private String databaseHost;
    private int databasePort;
    private String databaseName;
    private String databaseUser;
    private String databasePassword;

    // Cấu hình hệ thống từ database
    private double vatRate = 0.08; // Mặc định 8%
    private String shopName = "Coffee Shop";
    private String currency = "VND";

    private SystemConfigRepository configRepository;

    private AppConfig() {
        this.appName = "Coffee Shop Management";
        this.appVersion = "1.0.0";
        this.databaseHost = "localhost";
        this.databasePort = 3306;
        this.databaseName = "quanLyCaPheDb";
        this.databaseUser = "root";
        // Nhập mk mysql vào đây
        this.databasePassword = "thuandeptrai1601@";

        // Khởi tạo config repository
        this.configRepository = new SystemConfigRepository();
    }

    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
            instance.loadSystemConfig();
        }
        return instance;
    }

    /**
     * Load cấu hình từ database
     * Gọi method này sau khi database được kết nối thành công
     */
    public void loadSystemConfig() {
        try {
            String vatRateStr = configRepository.getConfigValue("VAT_RATE");
            if (vatRateStr != null) {
                this.vatRate = Double.parseDouble(vatRateStr);
                System.out.println("✅ Loaded VAT_RATE: " + this.vatRate);
            } else {
                System.out.println("⚠️ VAT_RATE not found in DB, using default: " + this.vatRate);
            }

            String shopNameStr = configRepository.getConfigValue("TEN_QUAN");
            if (shopNameStr != null) {
                this.shopName = shopNameStr;
                System.out.println("✅ Loaded TEN_QUAN: " + this.shopName);
            }

            String currencyStr = configRepository.getConfigValue("CURRENCY");
            if (currencyStr != null) {
                this.currency = currencyStr;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi load cấu hình hệ thống: " + e.getMessage());
            // Sử dụng giá trị mặc định nếu có lỗi
        }
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public void setDatabasePort(int databasePort) {
        this.databasePort = databasePort;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getJdbcUrl() {
        return "jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName
                + "?useSSL=false&serverTimezone=Asia/Bangkok&allowPublicKeyRetrieval=true";
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}