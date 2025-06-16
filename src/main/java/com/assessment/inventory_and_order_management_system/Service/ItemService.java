package com.assessment.inventory_and_order_management_system.Service;


import com.assessment.inventory_and_order_management_system.Model.Item;
import com.assessment.inventory_and_order_management_system.Repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item>  findAllItems() throws Exception {
        try {
            List<Item> items = itemRepository.findAll();
            log.info("Items: {}", items);
            return items;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Item findItemById(Long id) throws Exception {
        try {
            Item item = itemRepository.findById(id).orElse(null);
            log.info("Item: {}", item);
            return item;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Item saveItem(Item item) throws Exception {
        try {
            Item savedItem = itemRepository.save(item);
            log.info("Item saved: {}", savedItem);
            return savedItem;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Item updateItem(Item item) throws Exception {
        try {
            Item existingItem = itemRepository.findById(item.getId()).orElse(null);
            if (existingItem == null) {
                throw new Exception("Item not found");
            }
            else {
                existingItem.setName(item.getName());
                existingItem.setQuantity(item.getQuantity());
                existingItem.setPrice(item.getPrice());
                Item updatedItem = itemRepository.save(existingItem);
                log.info("Item updated: {}", updatedItem);
                return updatedItem;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteItem(Long id) throws Exception {
        try {
            itemRepository.deleteById(id);
            log.info("Item deleted with id: {}", id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
