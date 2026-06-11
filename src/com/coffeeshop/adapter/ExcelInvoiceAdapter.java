package com.coffeeshop.adapter;

import com.coffeeshop.model.Invoice;
import com.coffeeshop.model.InvoiceItem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

/*
 * Adapter Pattern:
 * Xuất hóa đơn ra XLSX bằng Apache POI.
 */
public class ExcelInvoiceAdapter implements InvoicePrinter {

    @Override
    public void print(Invoice invoice, String outputPath) throws Exception {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(outputPath)) {

            Sheet sheet = workbook.createSheet("HoaDon");

            int rowIndex = 0;
            Row titleRow = sheet.createRow(rowIndex++);
            titleRow.createCell(0).setCellValue("HOA DON BAN HANG");
            titleRow.createCell(1).setCellValue(invoice.getInvoiceId());

            rowIndex++;

            Row header = sheet.createRow(rowIndex++);
            header.createCell(0).setCellValue("Ten mon");
            header.createCell(1).setCellValue("So luong");
            header.createCell(2).setCellValue("Gia tai thoi diem");
            header.createCell(3).setCellValue("Thanh tien");

            for (InvoiceItem item : invoice.getItems()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(item.getDishName());
                row.createCell(1).setCellValue(item.getQuantityOrdered());
                row.createCell(2).setCellValue(item.getPriceAtTime());
                row.createCell(3).setCellValue(item.getTotalAmountAfterDiscount());
            }

            rowIndex++;
            Row total = sheet.createRow(rowIndex++);
            total.createCell(0).setCellValue("Tong tien chua thue");
            total.createCell(1).setCellValue(invoice.getSubtotal());

            Row vat = sheet.createRow(rowIndex++);
            vat.createCell(0).setCellValue("VAT");
            vat.createCell(1).setCellValue(invoice.getVatAmount());

            Row discount = sheet.createRow(rowIndex++);
            discount.createCell(0).setCellValue("Giam gia");
            discount.createCell(1).setCellValue(invoice.getDiscountAmount());

            Row finalTotal = sheet.createRow(rowIndex++);
            finalTotal.createCell(0).setCellValue("Thanh tien");
            finalTotal.createCell(1).setCellValue(invoice.getTotalAmount());

            workbook.write(fos);
        }
    }
}