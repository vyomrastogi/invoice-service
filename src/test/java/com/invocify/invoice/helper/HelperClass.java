package com.invocify.invoice.helper;


import java.math.BigDecimal;
import java.util.ArrayList;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.model.CompanyRequest;
import com.invocify.invoice.model.InvoiceRequest;
import com.invocify.invoice.model.InvoiceUpdateRequest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HelperClass {


	public static Invoice expectedInvoice(Company expectedCompany) {
		return Invoice.builder().author("tech guy")
				.company(expectedCompany)
				.lastModifiedDate(new Date())
				.id(UUID.randomUUID())
				.lineItems(new ArrayList<>())
				.build();
	}


    public static Invoice expectedPaidInvoice(Company expectedCompany) {
        LineItem lineItem = LineItem.builder().description("Service line item").quantity(1).rate(new BigDecimal(15.3))
                .rateType("flat").build();
        LineItem lineItem1 = LineItem.builder().description("line item").quantity(4).rate(new BigDecimal(10.3))
                .rateType("rate").build();
        List<LineItem> lineItemList = new ArrayList<LineItem>() {
            {
                add(lineItem);
                add(lineItem1);
            }
        };
        return Invoice.builder().author("tech guy")
                .company(expectedCompany)
                .lastModifiedDate(new Date())
                .id(UUID.randomUUID())
                .lineItems(lineItemList)
                .paidStatus(true)
                .build();

    }

    public static Invoice expectedUnPaidInvoice(Company expectedCompany) {
        LineItem lineItem = LineItem.builder().description("Service line item").quantity(1).rate(new BigDecimal(15.3))
                .rateType("flat").build();
        LineItem lineItem1 = LineItem.builder().description("line item").quantity(4).rate(new BigDecimal(10.3))
                .rateType("rate").build();
        List<LineItem> lineItemList = new ArrayList<LineItem>() {
            {
                add(lineItem);
                add(lineItem1);
            }
        };
        return Invoice.builder().author("tech guy")
                .company(expectedCompany)
                .lastModifiedDate(new Date())
                .id(UUID.randomUUID())
                .lineItems(lineItemList)
                .paidStatus(false)
                .build();

    }

    public static Company requestCompany() {
        return Company.builder().name("Amazon").street("233 Siliconvalley")
                .city("LA")
                .state("California")
                .postalCode("75035").build();
    }

    public static CompanyRequest requestCompanyRequest() {
        return CompanyRequest.builder().name("Amazon").street("233 Siliconvalley")
                .city("LA")
                .state("California")
                .postalCode("75035").build();
    }

    public static Company expectedCompany() {
        return Company.builder().id(UUID.randomUUID()).name("Amazon").street("233 Siliconvalley")
                .city("LA")
                .state("California")
                .postalCode("75035")
                .build();
    }

    public static InvoiceRequest requestInvoice(Invoice expectedInvoice) {
        return InvoiceRequest.builder().author(expectedInvoice.getAuthor()).company_id(expectedInvoice.getCompany().getId())
                .build();
    }

}
