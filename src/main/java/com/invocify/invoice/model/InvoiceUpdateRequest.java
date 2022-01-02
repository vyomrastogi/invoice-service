package com.invocify.invoice.model;

import com.invocify.invoice.entity.LineItem;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceUpdateRequest {

    @NotNull(message = "Author should be present")
    private String author;
    @NotNull(message="Invoice should be associated with an existing company")
    private UUID company_id;
    @NotEmpty(message="Atleast one line item should be present")
    @Valid
    private List<LineItem> lineItems;

    @Builder.Default
    private boolean paidStatus = false;

}
