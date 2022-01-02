package com.invocify.invoice.exception;

import java.util.UUID;

public class InvoiceNotFoundException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE_FMT = "Given Invoice not found: %s";

    public InvoiceNotFoundException(UUID invoiceId) {
        super(String.format(MESSAGE_FMT, invoiceId.toString()));
    }
}
