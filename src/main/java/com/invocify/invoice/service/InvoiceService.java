package com.invocify.invoice.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.invocify.invoice.exception.InvoiceAlreadyPaidException;
import com.invocify.invoice.exception.InvoiceNotFoundException;
import com.invocify.invoice.model.InvoiceUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.model.InvoiceRequest;
import com.invocify.invoice.repository.CompanyRepository;
import com.invocify.invoice.repository.InvoiceRepository;
import com.invocify.invoice.service.helper.InvocifyServiceHelper;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InvoiceService extends InvocifyServiceHelper {

	/**
	 * repository is getting injected using constructor provided by
	 * lombok.AllArgsConstructor
	 */
	InvoiceRepository invoiceRepository;
	CompanyRepository companyRepository;

	private static final int NUMBER_OF_ELEMENTS = 10;

	public Invoice createInvoice(InvoiceRequest invoiceRequest) throws InvalidCompanyException {
		Company company = getCompanyOrThrowException(companyRepository, invoiceRequest.getCompany_id());
		Invoice invoice = buildInvoiceEntity(invoiceRequest, company);
		return invoiceRepository.save(invoice);
	}

	private Invoice buildInvoiceEntity(InvoiceRequest invoiceRequest, Company company) {
		return new Invoice(invoiceRequest.getAuthor(), invoiceRequest.getLineItems(), company);
	}

	public Invoice addLineItemsToInvoice(UUID invoiceId, List<LineItem> lineItems) throws InvoiceNotFoundException, InvoiceAlreadyPaidException {
		Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
		if(invoice.isPaidStatus()) {
			throw new InvoiceAlreadyPaidException(invoiceId);
		}
		invoice.getLineItems().addAll(lineItems);
		return invoiceRepository.save(invoice);
	}

  public Page<Invoice> getInvoices(int page, long chronoValue, ChronoUnit chronoUnit , boolean disableFilter) {
		Pageable sortByDateWithTenEntries = PageRequest.of(page, NUMBER_OF_ELEMENTS,
				Sort.by("lastModifiedDate").descending());
		return disableFilter? invoiceRepository.findAll(sortByDateWithTenEntries) :
				invoiceRepository.findByLastModifiedDateAfter(sortByDateWithTenEntries, filterDate(chronoValue, chronoUnit));
	}

	private Date filterDate(long chronoValue, ChronoUnit chronoUnit) {
		LocalDateTime filterDate = LocalDateTime.now().minus(chronoValue, chronoUnit);
		return Date.from(filterDate.atZone(ZoneId.systemDefault()).toInstant());
	}

	public Invoice updateInvoice(UUID invoiceId, InvoiceUpdateRequest invoiceRequest) throws InvoiceNotFoundException, InvoiceAlreadyPaidException, InvalidCompanyException {
		Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
		if (invoice.isPaidStatus()){
			throw new InvoiceAlreadyPaidException(invoiceId);
		}
		Company company = getCompanyOrThrowException(companyRepository, invoiceRequest.getCompany_id());

		Invoice updatedInvoiceEntity = buildUpdatedInvoiceEntity(invoice,invoiceRequest,company);
		return invoiceRepository.save(updatedInvoiceEntity);
	}

	private Invoice buildUpdatedInvoiceEntity(Invoice invoice, InvoiceUpdateRequest invoiceRequest, Company company) {
		return Invoice.builder().id(invoice.getId())
				.author(invoiceRequest.getAuthor())
				.company(company)
				.lineItems(invoiceRequest.getLineItems())
				.paidStatus(invoiceRequest.isPaidStatus()).build();
	}
}
