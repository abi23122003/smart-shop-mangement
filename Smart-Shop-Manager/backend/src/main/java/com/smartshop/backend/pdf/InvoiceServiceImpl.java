package com.smartshop.backend.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
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

            // Fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

            // Title
            Paragraph title = new Paragraph("SMART SHOP MANAGER", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            // Invoice Details
            document.add(new Paragraph("Invoice No : " + sale.getSaleCode(), normalFont));
            document.add(new Paragraph("Sale Date  : " + sale.getSaleDate(), normalFont));
            document.add(new Paragraph("Customer   : " + sale.getCustomer().getCustomerName(), normalFont));

            document.add(new Paragraph(" "));

            // Product Table
            PdfPTable table = new PdfPTable(4);
           table.setWidthPercentage(90);
           table.setHorizontalAlignment(Element.ALIGN_CENTER);

           table.setWidths(new float[]{5,1,2,2}); 

            PdfPCell cell;

            cell = new PdfPCell(new Phrase("Product", headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Qty", headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Price", headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Total", headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            // Product Rows
            for (SaleItem item : sale.getSaleItems()) {

                table.addCell(new Phrase(item.getProduct().getProductName(), normalFont));

                PdfPCell qtyCell = new PdfPCell(
                        new Phrase(String.valueOf(item.getQuantity()), normalFont));
                qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(qtyCell);

                PdfPCell priceCell = new PdfPCell(
                        new Phrase(String.format("%.2f", item.getSellingPrice()), normalFont));
                priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(priceCell);

                PdfPCell totalCell = new PdfPCell(
                        new Phrase(String.format("%.2f", item.getTotalPrice()), normalFont));
                totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(totalCell);
            }

            document.add(table);

            document.add(new Paragraph(" "));

            Paragraph grandTotal = new Paragraph(
                    "Grand Total : ₹" + String.format("%.2f", sale.getTotalAmount()),
                    headerFont);

            grandTotal.setAlignment(Element.ALIGN_RIGHT);

            document.add(grandTotal);

            document.add(new Paragraph(" "));

            Paragraph thanks = new Paragraph(
                    "Thank You! Visit Again.",
                    headerFont);

            thanks.setAlignment(Element.ALIGN_CENTER);

            document.add(thanks);

            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}