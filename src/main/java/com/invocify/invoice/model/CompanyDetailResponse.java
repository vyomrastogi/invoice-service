package com.invocify.invoice.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "street", "city", "state", "postalCode", "active"})
public class CompanyDetailResponse extends CompanyBaseResponse {

  private UUID id;
  private String street;
  private String postalCode;
  private boolean active;
}
