package com.assessment.inventory_and_order_management_system.Controller;

import com.assessment.inventory_and_order_management_system.Model.Item;
import com.assessment.inventory_and_order_management_system.Service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/add")
    public ResponseEntity<Item> addItem(@RequestBody Item item) throws Exception {
        try {
            log.info("Adding item: {}", item);
            Item savedItem = itemService.saveItem(item);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            log.error("Error adding item: {}", item, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Item> findItemById(@PathVariable Long id) throws Exception {
        try {
            log.info("Finding item by id: {}", id);
            Item item = itemService.findItemById(id);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            log.error("Error finding item by id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) throws Exception {
        try {
            log.info("Deleting item with id: {}", id);
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting item with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> findAllItems() throws Exception {
        try {
            log.info("Finding all items");
            List<Item> items = itemService.findAllItems();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Error finding all items", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Item> updateItem(@RequestBody Item item) throws Exception {
        try {
            log.info("Updating item: {}", item);
            Item updatedItem = itemService.updateItem(item);
            return ResponseEntity.ok(updatedItem);
        } catch (Exception e) {
            log.error("Error updating item: {}", item, e);
            return ResponseEntity.badRequest().build();
        }
    }


}
