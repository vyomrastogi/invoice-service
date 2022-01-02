package com.invocify.invoice.model;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CompanyRequest {

	@NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "Street cannot be empty")
    private String street;
    @NotBlank(message = "City cannot be empty")
    private String city;
    @NotBlank(message = "State cannot be empty")
    private String state;
    @NotBlank(message = "PostalCode cannot be empty")
    private String postalCode;
    @Builder.Default
    private Boolean active = true;
}
