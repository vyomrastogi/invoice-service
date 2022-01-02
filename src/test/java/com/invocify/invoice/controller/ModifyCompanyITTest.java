package com.invocify.invoice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invocify.invoice.entity.Company;
import com.invocify.invoice.model.BaseResponse;
import com.invocify.invoice.model.CompanyRequest;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ModifyCompanyITTest {
	
	@Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
    
    private UUID id ;
    
    @Test
    public void modifyCompany() throws Exception {	
    	
    	 Company company = Company.builder()
    			 .name("Amazon")
    			 .street("233 Siliconvalley")
                 .city("LA")
                 .state("California")
                 .active(false)
                 .postalCode("75035").build();
    	 
    	 mockMvc.perform(post("/api/v1/invocify/companies")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(mapper.writeValueAsString(company)))
                 .andExpect(status().isCreated())
                 .andExpect(jsonPath("$.data.active").value(false))
                 .andDo(result -> {
                	 String response = result.getResponse().getContentAsString();
                	 Object data = mapper.readValue(response, BaseResponse.class).getData();
                	 setId(mapper.convertValue(data, Company.class).getId());
                	 
                 });
    	 
    	 mockMvc.perform(put("/api/v1/invocify/companies/{companyId}",id)
    			 .contentType(MediaType.APPLICATION_JSON)
                 .content(mapper.writeValueAsString(modifiedCompany())))
    	 .andExpect(status().isOk())
    	 .andExpect(jsonPath("$.data.id").value(id.toString()))
         .andExpect(jsonPath("$.data.name").value("Ofe's Company"))
         .andExpect(jsonPath("$.data.street").value("999 Fun Street"))
         .andExpect(jsonPath("$.data.city").value("Frisco"))
         .andExpect(jsonPath("$.data.state").value("TX"))
         .andExpect(jsonPath("$.data.postalCode").value("00000"))
         .andExpect(jsonPath("$.data.active").value(true));   	
    }
    
	@Test
	public void modifyCompany_InvalidCompany() throws Exception {

		setId(UUID.randomUUID());

		mockMvc.perform(put("/api/v1/invocify/companies/{companyId}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(modifiedCompany())))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.length()").value(1))
				.andExpect(jsonPath("$.errors[0]").value(String.format("Given company not found: %s",id.toString())));
	}
    
    private CompanyRequest modifiedCompany() {
		return CompanyRequest.builder().active(true)
		.city("Frisco")
		.name("Ofe's Company")
		.state("TX")
		.postalCode("00000")
		.street("999 Fun Street").build();
	}

	private void setId(UUID id) {
    	this.id = id;
    }

}
