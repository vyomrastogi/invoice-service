package com.invocify.invoice.exception;

import java.util.UUID;

public class InvalidCompanyException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FMT = "Given company not found: %s";
	
	public InvalidCompanyException (UUID company_id) {
		super(String.format(MESSAGE_FMT, company_id.toString()));
	}

}
