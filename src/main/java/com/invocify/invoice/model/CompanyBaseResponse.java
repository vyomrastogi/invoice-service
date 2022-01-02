package com.invocify.invoice.model;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CompanyBaseResponse {

	private UUID id;
    private String name;
    private String city;
    private String state;
}
