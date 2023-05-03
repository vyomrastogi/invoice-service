package com.invocify.invoice.model;

import com.invocify.invoice.entity.Invoice;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceListResponse {

  private List<Invoice> invoices;
  private int totalPages;
  private long totalElements;
}
