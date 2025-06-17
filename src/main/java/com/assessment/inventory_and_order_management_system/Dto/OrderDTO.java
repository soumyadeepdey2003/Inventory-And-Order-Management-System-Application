package com.assessment.inventory_and_order_management_system.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
	private Long id;

	@NotNull(message = "User ID is required")
	private Long userId;

	private String username;

	@NotEmpty(message = "Order must contain at least one item")
	private List<Long> itemIds;

	private List<ItemDTO> items;

	private BigDecimal totalPrice;

	@Builder.Default
	private LocalDateTime timestamp = LocalDateTime.now();
}