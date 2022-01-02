package com.invocify.invoice.exception;

import java.util.UUID;

public class InvoiceAlreadyPaidException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE_FMT = "Paid Invoice cannot be updated: %s";

    public InvoiceAlreadyPaidException(UUID invoiceId) {
        super(String.format(MESSAGE_FMT, invoiceId.toString()));
    }
}