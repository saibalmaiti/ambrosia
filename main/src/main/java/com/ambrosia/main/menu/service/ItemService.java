package com.ambrosia.main.menu.service;

import com.ambrosia.main.aws.service.StorageService;
import com.ambrosia.main.menu.entity.Item;
import com.ambrosia.main.menu.entity.ItemCategory;
import com.ambrosia.main.menu.dto.AddItemRequestDTO;
import com.ambrosia.main.menu.repository.ItemCategoryRepository;
import com.ambrosia.main.menu.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ItemService {
    ItemRepository itemRepository;
    ItemCategoryRepository itemCategoryRepository;
    StorageService storageService;
    @Autowired
    public ItemService(ItemRepository itemRepository, ItemCategoryRepository itemCategoryRepository, StorageService storageService) {
        this.itemRepository = itemRepository;
        this.itemCategoryRepository = itemCategoryRepository;
        this.storageService = storageService;
    }

    @Transactional
    @Modifying
    public Item addNewMenuItem(AddItemRequestDTO addItemRequest, String imageFileName) {
        String categoryName = addItemRequest.getCategory();
        ItemCategory category = itemCategoryRepository.findByName(categoryName);
        if(category == null) {
            category = ItemCategory.builder().name(addItemRequest.getCategory()).build();
        }
        Item item = Item.builder()
                .name(addItemRequest.getName())
                .description(addItemRequest.getDescription())
                .price(addItemRequest.getPrice())
                .isVeg(addItemRequest.getIsVeg())
                .isActive(true)
                .imageFileName(imageFileName)
                .category(category)
                .build();
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll(Sort.by(Sort.Order.by("itemId")));
    }

    public List<Item> getAllItemsByCategory(String category) {
        ItemCategory c = itemCategoryRepository.findByName(category);
        return  itemRepository.findAllByCategory(c);
    }

    @Modifying
    @Transactional
    public Item modifyItem(Item item) {
        return itemRepository.save(item);
    }

    @Modifying
    @Transactional
    public Item toggleActiveStatus(Long id) {
        Item item = itemRepository.getById(id);
        item.setIsActive(!item.getIsActive());
        return itemRepository.save(item);
    }

    @Modifying
    @Transactional
    public Item modifyImage(MultipartFile file, Long id) {
        Item item = itemRepository.getById(id);
        storageService.deleteFile(item.getImageFileName());
        String newFileName = storageService.uploadFile(file);
        item.setImageFileName(newFileName);

        return itemRepository.save(item);
    }

    @Modifying
    @Transactional
    public void deleteItem(Long id) {
        storageService.deleteFile(itemRepository.getById(id).getImageFileName());
        itemRepository.deleteById(id);
    }


}
