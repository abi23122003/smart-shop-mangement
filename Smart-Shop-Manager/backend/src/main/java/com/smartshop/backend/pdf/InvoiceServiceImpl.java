package com.smartshop.backend.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.smartshop.backend.entity.Sale;
import com.smartshop.backend.entity.SaleItem;
import com.smartshop.backend.repository.SaleRepository;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final SaleRepository saleRepository;

    public InvoiceServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public ByteArrayInputStream generateInvoice(Long saleId) {

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);

            document.open();

            // Shop Header
            document.add(new Paragraph("SMART SHOP MANAGER"));
            document.add(new Paragraph("===================================="));
            document.add(new Paragraph("Invoice No : " + sale.getSaleCode()));
            document.add(new Paragraph("Sale Date  : " + sale.getSaleDate()));
            document.add(new Paragraph("Customer   : " + sale.getCustomer().getCustomerName()));
            document.add(new Paragraph(" "));

            // Products
            document.add(new Paragraph("Products"));
            document.add(new Paragraph("------------------------------------"));

            for (SaleItem item : sale.getSaleItems()) {

                document.add(new Paragraph(
                        item.getProduct().getProductName()
                                + "   Qty: " + item.getQuantity()
                                + "   Price: ₹" + item.getSellingPrice()
                                + "   Total: ₹" + item.getTotalPrice()));
            }

            document.add(new Paragraph("------------------------------------"));
            document.add(new Paragraph("Grand Total : ₹" + sale.getTotalAmount()));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Thank You! Visit Again."));

            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}