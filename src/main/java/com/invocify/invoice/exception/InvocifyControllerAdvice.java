package com.invocify.invoice.exception;


import com.invocify.invoice.model.BaseResponse;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvocifyControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	public BaseResponse handleNotFound(MethodArgumentNotValidException methodArgumentNotValidException)
			throws Exception {
		BaseResponse response = new BaseResponse();
		methodArgumentNotValidException.getAllErrors().stream().map(ObjectError::getDefaultMessage)
				.forEach(error -> response.appendErrors(error));
		return response;
	}
    
    
    @ExceptionHandler(InvalidCompanyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse handleInvalidCompanyException(InvalidCompanyException invalidCompanyException) {
    	return generateResponse(invalidCompanyException.getMessage());
    }
	
    @ExceptionHandler(InvoiceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse handleInvoiceNotFoundException(InvoiceNotFoundException invoiceNotFoundException) {    	
    	return generateResponse(invoiceNotFoundException.getMessage());
      
    }

    @ExceptionHandler(InvoiceAlreadyPaidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse handleInvoiceAlreadyPaidException(InvoiceAlreadyPaidException invoiceAlreadyPaidException) {
    	return generateResponse(invoiceAlreadyPaidException.getMessage());
    }
    
    private BaseResponse generateResponse(String error) {
		BaseResponse response = new BaseResponse();
    	response.appendErrors(error);
        return response;
	}

}
