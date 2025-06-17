package com.assessment.inventory_and_order_management_system.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
	private Long id;

	@NotBlank(message = "Item name is required")
	private String name;

	@Min(value = 0, message = "Quantity must be greater than or equal to 0")
	private long quantity;

	@NotNull(message = "Price is required")
	@Min(value = 0, message = "Price must be greater than or equal to 0")
	private BigDecimal price;
}