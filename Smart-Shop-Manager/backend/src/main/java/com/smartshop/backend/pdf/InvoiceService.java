package com.smartshop.backend.pdf;

import java.io.ByteArrayInputStream;

public interface InvoiceService {

    ByteArrayInputStream generateInvoice(Long saleId);

}