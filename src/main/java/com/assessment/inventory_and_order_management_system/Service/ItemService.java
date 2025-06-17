package com.assessment.inventory_and_order_management_system.Service;

import com.assessment.inventory_and_order_management_system.Dto.ItemDTO;

import java.util.List;

public interface ItemService {
	ItemDTO saveItem(ItemDTO itemDTO);
	ItemDTO findItemById(Long id);
	List<ItemDTO> findAllItems();
	ItemDTO updateItem(ItemDTO itemDTO);
	void deleteItem(Long id);
	List<ItemDTO> findItemsByName(String name);
}