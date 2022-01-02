package com.invocify.invoice.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.*;

import com.invocify.invoice.model.InvoiceUpdateRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {

	@Id
	@GeneratedValue
	@Hidden
	private UUID id;

	@ManyToOne
	private Company company;

	private String author;

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<LineItem> lineItems;

	@Schema(example = "03-05-2021 17:30:29")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

	public Invoice(String author, List<LineItem> lineItems, Company company) {
		this.author = author;
		this.company = company;
		this.lineItems = lineItems;
	}

	@Schema(defaultValue = "false")
	@Setter
	private boolean paidStatus;

	/**
	 * Initializes date before saving the entity
	 */
	@PrePersist
	@PreUpdate
	private void prePersist() {
		this.lastModifiedDate = new Date();
	}

	/**
	 * Calculates the total cost of invoice by adding totalFees of lineItems
	 * @return total cost
	 */
	public BigDecimal getTotalCost() {
		return lineItems.stream().filter(Objects::nonNull).map(LineItem::getTotalFees)
				.reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_EVEN);
	}

}
