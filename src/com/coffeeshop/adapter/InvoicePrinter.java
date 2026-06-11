package com.coffeeshop.adapter;

import com.coffeeshop.model.Invoice;

public interface InvoicePrinter {
    void print(Invoice invoice, String outputPath) throws Exception;
}