package com.invocify.invoice.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Valid
public class Company {

    @Id
    @GeneratedValue
    private UUID id;
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
