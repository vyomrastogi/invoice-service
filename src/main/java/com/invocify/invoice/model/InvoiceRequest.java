package com.invocify.invoice.model;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.invocify.invoice.entity.LineItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

	@NotBlank(message="Author should be present")
    private String author;
    @NotNull(message="Invoice should be associated with an existing company")
    private UUID company_id;
    @NotEmpty(message="Atleast one line item should be present")
    @Valid
    private List<LineItem> lineItems;
}
