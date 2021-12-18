package com.ambrosia.main.menu;

import com.ambrosia.main.aws.service.StorageService;
import com.ambrosia.main.menu.entity.Item;
import com.ambrosia.main.menu.model.AddItemRequestDTO;
import com.ambrosia.main.menu.service.ItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {
    private final StorageService storageService;
    private final ItemService itemService;

    @Autowired
    public MenuController(StorageService storageService, ItemService itemService) {
        this.storageService = storageService;
        this.itemService = itemService;
    }

    @PostMapping("/add-item")
    public ResponseEntity<?> addNewItem(
            @RequestParam(value = "file")MultipartFile file,
            @RequestParam(value = "item") String jsonItem) {
        try {
            AddItemRequestDTO addItemRequest = new ObjectMapper().readValue(jsonItem, AddItemRequestDTO.class);
            String imageFileName = storageService.uploadFile(file); // storing to AWS and getting the image file name
            if(itemService.addNewMenuItem(addItemRequest, imageFileName) != null)
                return ResponseEntity.ok("Item successfully added");
            return ResponseEntity.badRequest().body("Failed to store the item in the database");
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to transform item string to json");
        }
    }

    @GetMapping("/get-all-items")
    public ResponseEntity<?> getAllItems() {
        return ResponseEntity.ok().body(itemService.getAllItems());
    }

    @GetMapping("/get-items-by-category")
    public ResponseEntity<?> getItemsByCategory(@RequestParam(value = "category") String category) {
        List<Item> items = itemService.getAllItemsByCategory(category);
        return ResponseEntity.ok().body(items);
    }


    @PutMapping("/modify-item")
    public ResponseEntity<?> modifyItem(@RequestBody Item item) {
        return ResponseEntity.ok().body(itemService.modifyItem(item));
    }

    @PutMapping("/modify-item-image")
    public ResponseEntity<?> modifyImage(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
        return ResponseEntity.ok().body(itemService.modifyImage(file, id));
    }

    @DeleteMapping("/delete-item")
    public ResponseEntity<?> deleteItem(@RequestParam("id") Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok("Item deleted successfully");
    }
}
