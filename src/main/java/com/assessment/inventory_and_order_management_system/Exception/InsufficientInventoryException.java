package com.assessment.inventory_and_order_management_system.Exception;

import org.springframework.http.HttpStatus;

public class InsufficientInventoryException extends ApiException {

	public InsufficientInventoryException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}

	public static InsufficientInventoryException forItem(String itemName) {
		return new InsufficientInventoryException("Item " + itemName + " is out of stock");
	}

	public static InsufficientInventoryException forItemWithQuantity(String itemName, long requestedQuantity, long availableQuantity) {
		return new InsufficientInventoryException("Insufficient inventory for item " + itemName +
				". Requested: " + requestedQuantity + ", Available: " + availableQuantity);
	}
}