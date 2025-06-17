package com.assessment.inventory_and_order_management_system.Service.Impl;

import com.assessment.inventory_and_order_management_system.Model.Item;
import com.assessment.inventory_and_order_management_system.Repository.ItemRepository;
import com.assessment.inventory_and_order_management_system.Service.ItemService;
import com.assessment.inventory_and_order_management_system.Dto.ItemDTO;
import com.assessment.inventory_and_order_management_system.Exception.DuplicateResourceException;
import com.assessment.inventory_and_order_management_system.Exception.InvalidRequestException;
import com.assessment.inventory_and_order_management_system.Exception.ResourceNotFoundException;
import com.assessment.inventory_and_order_management_system.Mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ItemDTO saveItem(ItemDTO itemDTO) {
        log.info("Saving item: {}", itemDTO);

        // Validate item data
        validateItem(itemDTO);

        // Check if item with the same name already exists
        if (itemRepository.existsByName(itemDTO.getName())) {
            throw DuplicateResourceException.forField("Item", "name", itemDTO.getName());
        }

        Item item = itemMapper.toEntity(itemDTO);
        Item savedItem = itemRepository.save(item);

        log.info("Item saved with ID: {}", savedItem.getId());
        return itemMapper.toDTO(savedItem);
    }

    @Override
    public ItemDTO findItemById(Long id) {
        log.info("Finding item by id: {}", id);

        Item item = itemRepository.findById(id)
            .orElseThrow(() -> ResourceNotFoundException.forId("Item", id));

        return itemMapper.toDTO(item);
    }

    @Override
    public List<ItemDTO> findAllItems() {
        log.info("Finding all items");

        List<Item> items = itemRepository.findAll();
        return items.stream()
            .map(itemMapper::toDTO)
            .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ItemDTO updateItem(ItemDTO itemDTO) {
        log.info("Updating item: {}", itemDTO);

        // Validate item data
        validateItem(itemDTO);

        // Check if item exists
        Item existingItem = itemRepository.findById(itemDTO.getId())
            .orElseThrow(() -> ResourceNotFoundException.forId("Item", itemDTO.getId()));

        // Check if name is being changed and if new name conflicts with existing items
        if (!existingItem.getName().equals(itemDTO.getName()) &&
            itemRepository.existsByName(itemDTO.getName())) {
            throw DuplicateResourceException.forField("Item", "name", itemDTO.getName());
        }

        Item item = itemMapper.toEntity(itemDTO);
        Item updatedItem = itemRepository.save(item);

        log.info("Item updated: {}", updatedItem);
        return itemMapper.toDTO(updatedItem);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteItem(Long id) {
        log.info("Deleting item with id: {}", id);

        // Check if item exists
        itemRepository.findById(id)
            .orElseThrow(() -> ResourceNotFoundException.forId("Item", id));

        itemRepository.deleteById(id);
        log.info("Item deleted with id: {}", id);
    }

    @Override
    public List<ItemDTO> findItemsByName(String name) {
        log.info("Finding items by name: {}", name);

        List<Item> items = itemRepository.findByNameContainingIgnoreCase(name);
        return items.stream()
            .map(itemMapper::toDTO)
            .toList();
    }

    private void validateItem(ItemDTO itemDTO) {
        if (itemDTO.getQuantity() < 0) {
            throw new InvalidRequestException("Item quantity cannot be negative");
        }

        if (itemDTO.getPrice() == null || itemDTO.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestException("Item price cannot be negative");
        }

        if (itemDTO.getName() == null || itemDTO.getName().trim().isEmpty()) {
            throw new InvalidRequestException("Item name cannot be empty");
        }
    }
}