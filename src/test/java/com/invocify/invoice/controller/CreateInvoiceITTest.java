package com.invocify.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.helper.HelperClass;
import com.invocify.invoice.model.InvoiceRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CreateInvoiceITTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private CompanyRepository companyRepository;


    @Test
    public void createInvoicewithLineItems_ValidateLineItems()  throws Exception {

        Company company = companyRepository.save(HelperClass.requestCompany());
        Invoice invoice = HelperClass.expectedInvoice(company);
        InvoiceRequest requestInvoice = HelperClass.requestInvoice(invoice);
        LineItem lineItem = LineItem.builder().description("Service line item").quantity(0).rate(new BigDecimal(15.3))
                .rateType("Ofe's Rate").build();
        LineItem lineItem1 = LineItem.builder().description("line item").quantity(-1).rate(new BigDecimal(10.3))
                .rateType("Vyom's Rate").build();
        requestInvoice.setLineItems(new ArrayList<LineItem>() {
            {
                add(lineItem);
                add(lineItem1);
            }
        });

        mockMvc
                .perform(post("/api/v1/invocify/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestInvoice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.length()").value(4))
                .andExpect(jsonPath("$.errors[0]").value("Quantity should be greater than Zero"))
                .andExpect(jsonPath("$.errors[1]").value("Quantity should be greater than Zero"))
                .andExpect(jsonPath("$.errors[2]").value("RateType should be flat or rate"))
                .andExpect(jsonPath("$.errors[3]").value("RateType should be flat or rate"))
        ;


    }
}
