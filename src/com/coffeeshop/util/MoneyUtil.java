package com.coffeeshop.util;

import java.text.NumberFormat;
import java.util.Locale;

public class MoneyUtil {
    private static final NumberFormat VN = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public static String format(double amount) {
        return VN.format(amount);
    }
}