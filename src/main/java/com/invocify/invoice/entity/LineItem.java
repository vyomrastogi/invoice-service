package com.invocify.invoice.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class LineItem {

    @Id
    @GeneratedValue
    @Hidden
    private UUID id;
    private String description;
    private BigDecimal rate;
    @Pattern(regexp = "flat|rate", message = "RateType should be flat or rate")
    private String rateType;
    @Builder.Default
    @Positive(message = "Quantity should be greater than Zero")
    private Integer quantity=1;


    @Hidden
    public BigDecimal getTotalFees() {
       return rate.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_EVEN);
    }

}
