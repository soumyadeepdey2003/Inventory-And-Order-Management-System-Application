package com.assessment.inventory_and_order_management_system.Controller;

import com.assessment.inventory_and_order_management_system.Service.ItemService;
import com.assessment.inventory_and_order_management_system.Dto.ItemDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO) {
        log.info("REST request to create Item: {}", itemDTO);
        ItemDTO result = itemService.saveItem(itemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id) {
        log.info("REST request to get Item by id: {}", id);
        ItemDTO itemDTO = itemService.findItemById(id);
        return ResponseEntity.ok(itemDTO);
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        log.info("REST request to get all Items");
        List<ItemDTO> items = itemService.findAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDTO>> searchItemsByName(@RequestParam String name) {
        log.info("REST request to search Items by name: {}", name);
        List<ItemDTO> items = itemService.findItemsByName(name);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(
        @PathVariable Long id,
        @Valid @RequestBody ItemDTO itemDTO) {
        log.info("REST request to update Item: {}", itemDTO);

        if (!id.equals(itemDTO.getId())) {
            log.error("Invalid ID");
            return ResponseEntity.badRequest().build();
        }

        ItemDTO result = itemService.updateItem(itemDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        log.info("REST request to delete Item: {}", id);
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}