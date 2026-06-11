package com.coffeeshop.adapter;

import com.coffeeshop.model.Invoice;
import com.coffeeshop.model.InvoiceItem;
import com.coffeeshop.util.MoneyUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

/*
 * Adapter Pattern:
 * Xuất hóa đơn ra TXT bằng thư viện có sẵn của Java.
 */
public class TxtInvoiceAdapter implements InvoicePrinter {

    @Override
    public void print(Invoice invoice, String outputPath) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(outputPath, StandardCharsets.UTF_8))) {

            writer.write("HOA DON BAN HANG");
            writer.newLine();
            writer.write("Ma HD: " + invoice.getInvoiceId());
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();

            for (InvoiceItem item : invoice.getItems()) {
                writer.write(item.getDishName() + " x" + item.getQuantityOrdered()
                        + " = " + MoneyUtil.format(item.getTotalAmountAfterDiscount()));
                writer.newLine();
            }

            writer.write("----------------------------------------");
            writer.newLine();
            writer.write("Tong truoc thue: " + MoneyUtil.format(invoice.getSubtotal()));
            writer.newLine();
            writer.write("VAT: " + MoneyUtil.format(invoice.getVatAmount()));
            writer.newLine();
            writer.write("Giam gia: " + MoneyUtil.format(invoice.getDiscountAmount()));
            writer.newLine();
            writer.write("Thanh tien: " + MoneyUtil.format(invoice.getTotalAmount()));
            writer.newLine();
            writer.write("Tien khach dua: " + MoneyUtil.format(invoice.getAmountPaid()));
            writer.newLine();
            writer.write("Tien tra lai: " + MoneyUtil.format(invoice.getChangeAmount()));
        }
    }
}