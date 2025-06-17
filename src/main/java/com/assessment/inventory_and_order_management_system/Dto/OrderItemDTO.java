package com.assessment.inventory_and_order_management_system.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
	private Long id;

	@NotNull(message = "Item ID is required")
	private Long itemId;

	private ItemDTO item;

	@Min(value = 1, message = "Quantity must be at least 1")
	private long quantity;

	private Long orderId;
}