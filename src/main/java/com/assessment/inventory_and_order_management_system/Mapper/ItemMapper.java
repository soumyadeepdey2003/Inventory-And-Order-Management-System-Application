package com.assessment.inventory_and_order_management_system.Mapper;

import com.assessment.inventory_and_order_management_system.Model.Item;
import com.assessment.inventory_and_order_management_system.Dto.ItemDTO;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

	public ItemDTO toDTO(Item item) {
		if (item == null) {
			return null;
		}

		return ItemDTO.builder()
				.id(item.getId())
				.name(item.getName())
				.quantity(item.getQuantity())
				.price(item.getPrice())
				.build();
	}

	public Item toEntity(ItemDTO itemDTO) {
		if (itemDTO == null) {
			return null;
		}

		Item item = new Item();
		item.setId(itemDTO.getId());
		item.setName(itemDTO.getName());
		item.setQuantity(itemDTO.getQuantity());
		item.setPrice(itemDTO.getPrice());

		return item;
	}
}