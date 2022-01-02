package com.invocify.invoice.service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.helper.HelperClass;
import com.invocify.invoice.model.InvoiceRequest;
import com.invocify.invoice.repository.CompanyRepository;
import com.invocify.invoice.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class InvoiceServiceTest {

    InvoiceService invoiceService;
    InvoiceRepository invoiceRepository;
    CompanyRepository companyRepository;

    @BeforeEach
    void init() {
        invoiceRepository = mock(InvoiceRepository.class);
        companyRepository = mock(CompanyRepository.class);
        invoiceService = new InvoiceService(invoiceRepository, companyRepository);
    }

    @Test
    public void createInvoice() throws InvalidCompanyException {
        Company expectedCompany = HelperClass.expectedCompany();
        Invoice expectedInvoice = HelperClass.expectedInvoice(expectedCompany);

        //mocked repositories
        when(companyRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(expectedCompany));
        when(invoiceRepository.save(Mockito.any(Invoice.class))).thenReturn(expectedInvoice);

        InvoiceRequest requestInvoice = HelperClass.requestInvoice(expectedInvoice);
        requestInvoice.setLineItems(new ArrayList<>());
        Invoice actualInvoice = invoiceService.createInvoice(requestInvoice);

        //asserts
        assertEquals(expectedInvoice, actualInvoice);
        assertEquals(expectedCompany, actualInvoice.getCompany());
        assertEquals(BigDecimal.ZERO.setScale(2), actualInvoice.getTotalCost());
        assertNotNull(actualInvoice.getId());
        assertNotNull(actualInvoice.getLastModifiedDate());

        //verify mock usage
        verify(companyRepository, times(1)).findById(Mockito.any(UUID.class));
        verify(invoiceRepository, times(1)).save(Mockito.any(Invoice.class));
    }   


}
