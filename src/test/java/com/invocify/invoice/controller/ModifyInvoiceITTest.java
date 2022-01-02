package com.invocify.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.helper.HelperClass;
import com.invocify.invoice.model.InvoiceRequest;
import com.invocify.invoice.model.InvoiceUpdateRequest;
import com.invocify.invoice.repository.CompanyRepository;
import com.invocify.invoice.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ModifyInvoiceITTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void addLineItemsToNonExistingInvoice() throws Exception {
        LineItem lineItem3 = LineItem.builder().description("flat line item3").quantity(1).rate(new BigDecimal(5.5))
                .rateType("flat").build();
        LineItem lineItem4 = LineItem.builder().description("rate based line item4").quantity(3).rate(new BigDecimal(5.7))
                .rateType("rate").build();
        List<LineItem> patchLineItems = new ArrayList<>();
        patchLineItems.add(lineItem3);
        patchLineItems.add(lineItem4);
        UUID randomUUID = UUID.randomUUID();
        mockMvc.perform(patch("/api/v1/invocify/invoices/{invoiceId}/lineItems", randomUUID )
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patchLineItems))).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.[0]").value(String.format("Given Invoice not found: %s", randomUUID.toString())));
    }

    @Test
    public void addLineItemsToExistingUnpaidInvoice() throws Exception {
        Company company = companyRepository.save(HelperClass.requestCompany());
        Invoice invoice = HelperClass.expectedInvoice(company);
        InvoiceRequest requestInvoice = HelperClass.requestInvoice(invoice);
        LineItem lineItem = LineItem.builder().description("Service line item").quantity(1).rate(new BigDecimal(15.3))
                .rateType("flat").build();
        LineItem lineItem1 = LineItem.builder().description("line item").quantity(4).rate(new BigDecimal(10.3))
                .rateType("rate").build();
        requestInvoice.setLineItems(new ArrayList<LineItem>() {
            {
                add(lineItem);
                add(lineItem1);
            }
        });
        mockMvc.perform(post("/api/v1/invocify/invoices").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestInvoice))).andExpect(status().isCreated());
        Invoice invoice1 = invoiceRepository.findAll().get(0);
        LineItem lineItem3 = LineItem.builder().description("flat line item3").quantity(1).rate(new BigDecimal(5.5))
                .rateType("flat").build();
        LineItem lineItem4 = LineItem.builder().description("rate based line item4").quantity(3).rate(new BigDecimal(5.7))
                .rateType("rate").build();
        List<LineItem> patchLineItems = new ArrayList<>();
        patchLineItems.add(lineItem3);
        patchLineItems.add(lineItem4);
        mockMvc.perform(patch("/api/v1/invocify/invoices/{invoiceId}/lineItems",invoice1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patchLineItems))).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists()).andExpect(jsonPath("$.data.author").value(requestInvoice.getAuthor()))
                .andExpect(jsonPath("$.data.lastModifiedDate").value(not(invoice1.getLastModifiedDate())))
                .andExpect(jsonPath("$.data.totalCost").value(79.1))
                .andExpect(jsonPath("$.data.company.id").value(company.getId().toString()))
                .andExpect(jsonPath("$.data.company.name").value(company.getName()))
                .andExpect(jsonPath("$.data.company.street").value(company.getStreet()))
                .andExpect(jsonPath("$.data.company.city").value(company.getCity()))
                .andExpect(jsonPath("$.data.company.state").value(company.getState()))
                .andExpect(jsonPath("$.data.company.postalCode").value(company.getPostalCode()))
                .andExpect(jsonPath("$.data.lineItems.length()").value(4))
                .andExpect(jsonPath("$.data.lineItems[2].id").exists())
                .andExpect(jsonPath("$.data.lineItems[2].description").value("flat line item3"))
                .andExpect(jsonPath("$.data.lineItems[2].quantity").value(1))
                .andExpect(jsonPath("$.data.lineItems[2].rateType").value("flat"))
                .andExpect(jsonPath("$.data.lineItems[2].rate").value(5.5))
                .andExpect(jsonPath("$.data.lineItems[2].totalFees").value(5.5))
                .andExpect(jsonPath("$.data.lineItems[3].id").exists())
                .andExpect(jsonPath("$.data.lineItems[3].description").value("rate based line item4"))
                .andExpect(jsonPath("$.data.lineItems[3].quantity").value(3))
                .andExpect(jsonPath("$.data.lineItems[3].rateType").value("rate"))
                .andExpect(jsonPath("$.data.lineItems[3].rate").value(5.7))
                .andExpect(jsonPath("$.data.lineItems[3].totalFees").value(17.1));
    }

    @Test
    public void addLineItemsToExistingPaidInvoice() throws Exception {

        Invoice invoice = getPaidInvoiceWithTwoLineItemsAndCompany();
        LineItem lineItem3 = LineItem.builder().description("flat line item3").quantity(1).rate(new BigDecimal(5.5))
                .rateType("flat").build();
        LineItem lineItem4 = LineItem.builder().description("rate based line item4").quantity(3).rate(new BigDecimal(5.7))
                .rateType("rate").build();
        List<LineItem> patchLineItems = new ArrayList<>();
        patchLineItems.add(lineItem3);
        patchLineItems.add(lineItem4);
        mockMvc.perform(patch("/api/v1/invocify/invoices/{invoiceId}/lineItems",invoice.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patchLineItems)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value(String.format("Paid Invoice cannot be updated: %s", invoice.getId().toString())));

    }


    @Test
    public void updateExistingUnpaidInvoice() throws Exception {
        Invoice invoice = getUnPaidInvoiceWithTwoLineItemsAndCompany();
        Company company = Company.builder().name("Apple").street("430 CreditValley")
                .city("New York")
                .state("New York")
                .postalCode("75036").build();
        company = companyRepository.save(company);
        LineItem lineItem2 = LineItem.builder().description("Service line item 2").quantity(1).rate(new BigDecimal(150.3))
                .rateType("flat").build();

        List<LineItem> lineItemList = new ArrayList<>();
        lineItemList.add(invoice.getLineItems().get(0));
        lineItemList.add(lineItem2);

        InvoiceUpdateRequest invoiceUpdateRequest = new InvoiceUpdateRequest("tech person",company.getId(),lineItemList, true);

        mockMvc.perform(put("/api/v1/invocify/invoices/"+invoice.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoiceUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(invoice.getId().toString())).andExpect(jsonPath("$.data.author").value(invoiceUpdateRequest.getAuthor()))
                .andExpect(jsonPath("$.data.lastModifiedDate").value(not(invoice.getLastModifiedDate())))
                .andExpect(jsonPath("$.data.totalCost").value(165.6))
                .andExpect(jsonPath("$.data.paidStatus").value(true))
                .andExpect(jsonPath("$.data.company.id").value(company.getId().toString()))
                .andExpect(jsonPath("$.data.company.name").value(company.getName()))
                .andExpect(jsonPath("$.data.company.street").value(company.getStreet()))
                .andExpect(jsonPath("$.data.company.city").value(company.getCity()))
                .andExpect(jsonPath("$.data.company.state").value(company.getState()))
                .andExpect(jsonPath("$.data.company.postalCode").value(company.getPostalCode()))
                .andExpect(jsonPath("$.data.lineItems.length()").value(2))
                .andExpect(jsonPath("$.data.lineItems[0].id").exists())
                .andExpect(jsonPath("$.data.lineItems[0].description").value("Service line item"))
                .andExpect(jsonPath("$.data.lineItems[0].quantity").value(1))
                .andExpect(jsonPath("$.data.lineItems[0].rateType").value("flat"))
                .andExpect(jsonPath("$.data.lineItems[0].rate").value(15.3))
                .andExpect(jsonPath("$.data.lineItems[0].totalFees").value(15.3))
                .andExpect(jsonPath("$.data.lineItems[1].id").exists())
                .andExpect(jsonPath("$.data.lineItems[1].description").value("Service line item 2"))
                .andExpect(jsonPath("$.data.lineItems[1].quantity").value(1))
                .andExpect(jsonPath("$.data.lineItems[1].rateType").value("flat"))
                .andExpect(jsonPath("$.data.lineItems[1].rate").value(150.3))
                .andExpect(jsonPath("$.data.lineItems[1].totalFees").value(150.3));

    }

    @Test
    public void updatePaidInvoice() throws Exception {
        Invoice invoice = getPaidInvoiceWithTwoLineItemsAndCompany();
        Company company = Company.builder().name("Apple").street("430 CreditValley")
                .city("New York")
                .state("New York")
                .postalCode("75036").build();
        company = companyRepository.save(company);
        LineItem lineItem2 = LineItem.builder().description("Service line item 2").quantity(1).rate(new BigDecimal(150.3))
                .rateType("flat").build();

        List<LineItem> lineItemList = new ArrayList<>();
        lineItemList.add(invoice.getLineItems().get(0));
        lineItemList.add(lineItem2);

        InvoiceUpdateRequest invoiceUpdateRequest = new InvoiceUpdateRequest("tech person",company.getId(),lineItemList, false);

        mockMvc.perform(put("/api/v1/invocify/invoices/"+invoice.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoiceUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0]").value(String.format("Paid Invoice cannot be updated: %s", invoice.getId().toString())));

    }

    @Test
    public void updateNonExistingInvoice() throws Exception {
        UUID invoiceRandomUUID = UUID.randomUUID();
        Company company = Company.builder().name("Apple").street("430 CreditValley")
                .city("New York")
                .state("New York")
                .postalCode("75036").build();
        company = companyRepository.save(company);
        LineItem lineItem2 = LineItem.builder().description("Service line item 2").quantity(1).rate(new BigDecimal(150.3))
                .rateType("flat").build();

        List<LineItem> lineItemList = new ArrayList<>();
        lineItemList.add(lineItem2);

        InvoiceUpdateRequest invoiceUpdateRequest = new InvoiceUpdateRequest("tech person",company.getId(),lineItemList, false);

        mockMvc.perform(put("/api/v1/invocify/invoices/"+invoiceRandomUUID).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoiceUpdateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.[0]").value(String.format("Given Invoice not found: %s", invoiceRandomUUID)));

    }

    @Test
    public void updateExistingInvoiceWithUnavailableCompany() throws Exception {
        Invoice invoice = getUnPaidInvoiceWithTwoLineItemsAndCompany();
        UUID companyRandomUUID = UUID.randomUUID();

        LineItem lineItem2 = LineItem.builder().description("Service line item 2").quantity(1).rate(new BigDecimal(150.3))
                .rateType("flat").build();

        List<LineItem> lineItemList = new ArrayList<>();
        lineItemList.add(lineItem2);

        InvoiceUpdateRequest invoiceUpdateRequest = new InvoiceUpdateRequest("tech person",companyRandomUUID,lineItemList, false);

        mockMvc.perform(put("/api/v1/invocify/invoices/"+invoice.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoiceUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0]").value(String.format("Given company not found: %s", companyRandomUUID.toString())));

    }

    @Test
    public void updateExistingInvoiceWithInvalidRequest() throws Exception {
        Invoice invoice = getUnPaidInvoiceWithTwoLineItemsAndCompany();

        InvoiceUpdateRequest invoiceUpdateRequest = new InvoiceUpdateRequest();

        mockMvc.perform(put("/api/v1/invocify/invoices/"+invoice.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoiceUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.length()").value(3))
                .andExpect(jsonPath("$.errors.[0]").value("Atleast one line item should be present"))
                .andExpect(jsonPath("$.errors.[1]").value("Author should be present"))
                .andExpect(jsonPath("$.errors.[2]").value("Invoice should be associated with an existing company"));

    }

    /**********************
        UTILITY METHODS
     **********************/
    private Invoice getUnPaidInvoiceWithTwoLineItemsAndCompany() throws Exception {
        Company company = companyRepository.save(HelperClass.requestCompany());
        Invoice invoice = HelperClass.expectedUnPaidInvoice(company);
        return invoiceRepository.save(invoice);
    }

    private Invoice getPaidInvoiceWithTwoLineItemsAndCompany() throws Exception {
        Company company = companyRepository.save(HelperClass.requestCompany());
        Invoice invoice = HelperClass.expectedPaidInvoice(company);
        return invoiceRepository.save(invoice);
    }
}
